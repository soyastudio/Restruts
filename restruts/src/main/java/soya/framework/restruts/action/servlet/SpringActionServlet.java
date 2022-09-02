package soya.framework.restruts.action.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import soya.framework.restruts.action.*;
import soya.framework.restruts.action.servlet.api.Swagger;
import soya.framework.restruts.action.servlet.io.DefaultServletStreamHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SpringActionServlet extends HttpServlet {

    public static final String INIT_PARAM_STREAM_HANDLER = "soya.framework.action.STREAM_HANDLER";

    private Swagger swagger;
    private Registry registry = new Registry();


    private ServletStreamHandler streamHandler;
    private DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Swagger.SwaggerBuilder builder = Swagger.builder();

        String path = this.getServletInfo();
        ServletRegistration registration = config.getServletContext().getServletRegistration(getServletName());
        for (String e : registration.getMappings()) {
            if (e.endsWith("/*")) {
                path = e.substring(0, e.lastIndexOf("/*"));
            }
        }
        builder.basePath(path);

        ActionMappings actionMappings = ActionContext.getInstance().getActionMappings();
        for (String dm : actionMappings.domains()) {
            Domain domain = actionMappings.domainType(dm).getAnnotation(Domain.class);
            builder.addTag(Swagger.TagObject.instance()
                    .name(domain.title().isEmpty() ? domain.name() : domain.title())
                    .description(domain.description()));
        }

        ActionName[] actionNames = actionMappings.actions(null);
        for (ActionName actionName : actionNames) {
            Class<? extends ActionCallable> cls = actionMappings.actionType(actionName);
            OperationMapping operationMapping = cls.getAnnotation(OperationMapping.class);

            String operationId = operationMapping.name().isEmpty() ? cls.getSimpleName() : operationMapping.name();

            OperationMapping.HttpMethod httpMethod = operationMapping.method();
            Swagger.PathBuilder pathBuilder = null;
            if (httpMethod.equals(OperationMapping.HttpMethod.GET)) {
                pathBuilder = builder.get(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.POST)) {
                pathBuilder = builder.post(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.DELETE)) {
                pathBuilder = builder.delete(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.PUT)) {
                pathBuilder = builder.put(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.HEAD)) {
                pathBuilder = builder.head(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.OPTIONS)) {
                pathBuilder = builder.options(operationMapping.path(), operationId);

            } else if (httpMethod.equals(OperationMapping.HttpMethod.PATCH)) {
                pathBuilder = builder.patch(operationMapping.path(), operationId);

            }

            pathBuilder.description(operationMapping.description());

            if (pathBuilder != null) {
                for (Field f : actionMappings.parameterFields(cls)) {
                    if (f.getAnnotation(ParameterMapping.class) != null) {
                        ParameterMapping param = f.getAnnotation(ParameterMapping.class);
                        String name = param.name().isEmpty() ? f.getName() : param.name();
                        String in = null;
                        if (ParameterMapping.ParameterType.PATH_PARAM.equals(param.parameterType())) {
                            in = "path";
                        } else if (ParameterMapping.ParameterType.QUERY_PARAM.equals(param.parameterType())) {
                            in = "query";
                        } else if (ParameterMapping.ParameterType.HEADER_PARAM.equals(param.parameterType())) {
                            in = "header";
                        } else if (ParameterMapping.ParameterType.COOKIE_PARAM.equals(param.parameterType())) {
                            in = "cookie";
                        }
                        pathBuilder.parameterBuilder(name, in, param.description()).build();

                    } else if (f.getAnnotation(PayloadMapping.class) != null) {
                        PayloadMapping payload = f.getAnnotation(PayloadMapping.class);
                        String name = payload.name().isEmpty() ? f.getName() : payload.name();
                        pathBuilder.bodyParameterBuilder(name, payload.description())
                                .build()
                                .consumes(payload.consumes());

                    }
                }
            }

            Class<?> domainClass = actionMappings.domainType(operationMapping.domain());
            if (domainClass != null) {
                Domain domain = domainClass.getAnnotation(Domain.class);
                pathBuilder
                        .addTag(domain.title().isEmpty() ? domain.name() : domain.title())
                        .produces(operationMapping.produces());

            } else {
                pathBuilder
                        .addTag(operationMapping.domain())
                        .produces(operationMapping.produces());

            }

            pathBuilder.build();

            registry.put(new Registration(operationMapping.method().name(), operationMapping.path()), cls);

        }

        this.swagger = builder.build();

        if (config.getInitParameter(INIT_PARAM_STREAM_HANDLER) != null) {

        } else {
            this.streamHandler = new DefaultServletStreamHandler();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().equals("/swagger.json")) {
            PrintWriter writer = resp.getWriter();
            writer.print(swagger.toJson());

            writer.flush();
            writer.close();
        } else {
            dispatch(req, resp);
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    protected void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ActionRequestEvent event = new ActionRequestEvent(req);

        if (!registry.containsKey(event.registration)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            AsyncContext asyncContext = req.startAsync();
            asyncContext.start(() -> {
                try {
                    ActionCallable action = registry.create(event);

                    ActionResult actionResult = action.call();

                    if (actionResult.success()) {
                        streamHandler.write(actionResult.get(), req, resp);

                    } else {
                        exceptionHandler.onException((Exception) actionResult.get(), req, resp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                } finally {
                    asyncContext.complete();
                }
            });
        }
    }

    static class Registry extends AbstractMap<Registration, Class<? extends ActionCallable>> {

        private Set<Entry<Registration, Class<? extends ActionCallable>>> entrySet = new HashSet<>();

        @Override
        public Set<Entry<Registration, Class<? extends ActionCallable>>> entrySet() {
            return entrySet;
        }

        public Class<? extends ActionCallable> get(Registration registration) {
            Iterator<Entry<Registration, Class<? extends ActionCallable>>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<Registration, Class<? extends ActionCallable>> e = i.next();
                if (registration.equals(e.getKey()))
                    return e.getValue();
            }
            return null;
        }

        @Override
        public Class<? extends ActionCallable> put(Registration key, Class<? extends ActionCallable> value) {
            Map.Entry<Registration, Class<? extends ActionCallable>> entry = new SimpleEntry<>(key, value);
            entrySet.add(entry);
            return entry.getValue();
        }

        ActionCallable create(ActionRequestEvent event) throws Exception {
            Class<? extends ActionCallable> actionType = get(event.registration);
            ActionCallable action = actionType.newInstance();
            HttpServletRequest httpServletRequest = (HttpServletRequest) event.getSource();

            Field[] fields = ActionContext.getInstance().getActionMappings().parameterFields(actionType);
            for (Field field : fields) {
                Class<?> paramType = field.getType();
                Object value = null;
                if (field.getAnnotation(PayloadMapping.class) != null) {
                    if (paramType.equals(String.class)) {
                        value = event.getPayload(paramType);
                    }

                } else if (field.getAnnotation(ParameterMapping.class) != null) {
                    ParameterMapping parameterMapping = field.getAnnotation(ParameterMapping.class);
                    String name = parameterMapping.name().isEmpty() ? field.getName() : parameterMapping.name();
                    String paramValue = null;
                    if (ParameterMapping.ParameterType.HEADER_PARAM.equals(parameterMapping.parameterType())) {
                        paramValue = httpServletRequest.getHeader(name);

                    } else if (ParameterMapping.ParameterType.QUERY_PARAM.equals(parameterMapping.parameterType())) {
                        paramValue = event.queryParams.get(name).get(0);

                    } else if (ParameterMapping.ParameterType.QUERY_PARAM.equals(parameterMapping.parameterType())) {
                        paramValue = event.getQueryParameter(name);

                    } else if (ParameterMapping.ParameterType.COOKIE_PARAM.equals(parameterMapping.parameterType())) {
                        // TODO:
                    }

                    value = ConvertUtils.convert(paramValue, field.getType());
                }

                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(action, value);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            return action;
        }
    }

    static class Registration implements Comparable<Registration> {
        private final String method;
        private final String path;
        private String[] paths;

        Registration(String method, String path) {
            this.method = method.toLowerCase();
            this.path = path.trim();
            this.paths = path.split("/");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof Registration)) return false;

            Registration that = (Registration) o;
            if (!method.equals(that.method)) {
                return false;
            }

            if (path.equals(that.path)) {
                return true;
            }

            if (paths.length != that.paths.length) {
                return false;
            }

            for (int i = 0; i < paths.length; i++) {
                if (!paths[i].equals(that.paths[i]) && !(that.paths[i].contains("{") && that.paths[i].contains("}"))) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int compareTo(Registration o) {
            int result = 0;
            int size = Math.min(paths.length, o.paths.length);
            for (int i = 0; i < size; i++) {
                result = paths[i].compareTo(o.paths[i]);
                if (result != 0) {
                    return result;
                }
            }

            result = paths.length - o.paths.length;
            if (result == 0) {
                result = method.compareTo(o.method);
            }

            return result;
        }
    }

    static class ActionRequestEvent extends EventObject {
        private Registration registration;
        private Map<String, List<String>> queryParams = new HashMap<>();
        private byte[] inputData;

        ActionRequestEvent(HttpServletRequest request) {
            super(request);
            this.registration = new Registration(request.getMethod(), request.getPathInfo());

            request.getPathInfo();

            String encoding = "UTF-8";
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                final String[] pairs = query.split("&");
                for (String pair : pairs) {
                    try {
                        final int idx = pair.indexOf("=");
                        final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), encoding) : pair;
                        if (!queryParams.containsKey(key)) {
                            queryParams.put(key, new LinkedList<String>());
                        }
                        final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), encoding) : null;
                        queryParams.get(key).add(value);

                    } catch (Exception e) {

                    }
                }
            }

            try {
                ServletInputStream inputStream = request.getInputStream();
                if (inputStream != null && inputStream.available() > 0) {
                    inputData = new byte[request.getContentLength()];

                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    while ((nRead = inputStream.read(inputData, 0, inputData.length)) != -1) {
                        buffer.write(inputData, 0, nRead);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        public String getHeader(String name) {
            HttpServletRequest request = (HttpServletRequest) getSource();
            return request.getHeader(name);
        }

        public String getQueryParameter(String name) {
            if (queryParams.containsKey(name)) {
                return queryParams.get(name).size() > 0 ? queryParams.get(name).get(0) : null;
            } else {
                return null;
            }
        }

        public <T> T getPayload(Class<T> type) {
            if (type.getName().equals("java.lang.String")) {
                return (T) new String(inputData);
            }

            return null;
        }
    }

    static class DefaultExceptionHandler implements ExceptionHandler<Exception> {
        private Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        @Override
        public void onException(Exception exception, HttpServletRequest request, HttpServletResponse response) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("exception", exception.getClass().getName());
            jsonObject.addProperty("cause", exception.getCause().getClass().getName());
            jsonObject.addProperty("message", exception.getMessage());

            try {
                response.getOutputStream().write(GSON.toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
