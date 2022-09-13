package soya.framework.action.servlet;

import org.reflections.Reflections;
import soya.framework.action.*;
import soya.framework.action.servlet.api.Swagger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class ActionServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(ActionServlet.class.getName());

    public static final String INIT_PARAM_STREAM_HANDLER = "soya.framework.action.STREAM_HANDLER";

    private Swagger swagger;
    private Registry registry = new Registry();

    private Map<String, StreamWriter> readers = new HashMap<>();
    private Map<String, StreamWriter> writers = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initStreamHandlers(config);
        initSwagger(config);

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
                    write(actionResult.get(), req, resp);

                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    throw new RuntimeException(e);

                } finally {
                    asyncContext.complete();

                }
            });
        }
    }

    protected void write(Object object, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (object == null) {

        } else if (object instanceof Exception) {
            printException((Exception) object, response);

        } else {
            String contentType = request.getHeader("Accept");
            response.setHeader("Content-Type", contentType);

            OutputStream outputStream = response.getOutputStream();
            if (object instanceof String) {
                outputStream.write(((String) object).getBytes(StandardCharsets.UTF_8));

            } else {
                if (MediaType.TEXT_PLAIN.equals(contentType)) {
                    outputStream.write(object.toString().getBytes(StandardCharsets.UTF_8));

                } else if (MediaType.APPLICATION_JSON.equalsIgnoreCase(contentType)) {
                    writers.get(contentType).write(object, outputStream);

                }
            }

        }

    }

    protected void printException(Exception exception, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(MediaType.TEXT_PLAIN);
        try {
            response.getWriter().print(exception.getMessage());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initStreamHandlers(ServletConfig config) {
        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(StreamHandler.class);
        classes.forEach(e -> {
            StreamHandler handler = e.getAnnotation(StreamHandler.class);
            String mediaType = handler.value();
            try {
                Object object = e.newInstance();
                if (object instanceof StreamWriter) {
                    writers.put(mediaType, (StreamWriter) object);
                }

                if (object instanceof StreamReader) {
                    readers.put(mediaType, (StreamWriter) object);
                }

            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

        });
    }

    protected void initSwagger(ServletConfig config) {

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
            ActionClass actionClass = actionMappings.actionClass(actionName);
            Class<? extends ActionCallable> cls = actionClass.getActionType();
            ActionDefinition actionDefinition = cls.getAnnotation(ActionDefinition.class);

            String operationId = actionDefinition.name().isEmpty() ? cls.getSimpleName() : actionDefinition.name();

            ActionDefinition.HttpMethod httpMethod = actionDefinition.method();
            Swagger.PathBuilder pathBuilder = null;
            if (httpMethod.equals(ActionDefinition.HttpMethod.GET)) {
                pathBuilder = builder.get(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.POST)) {
                pathBuilder = builder.post(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.DELETE)) {
                pathBuilder = builder.delete(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.PUT)) {
                pathBuilder = builder.put(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.HEAD)) {
                pathBuilder = builder.head(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.OPTIONS)) {
                pathBuilder = builder.options(actionDefinition.path(), operationId);

            } else if (httpMethod.equals(ActionDefinition.HttpMethod.PATCH)) {
                pathBuilder = builder.patch(actionDefinition.path(), operationId);

            }

            pathBuilder.description(actionDefinition.description());

            if (pathBuilder != null) {
                for (Field f : actionClass.getActionFields()) {
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

            Class<?> domainClass = actionMappings.domainType(actionDefinition.domain());
            if (domainClass != null) {
                Domain domain = domainClass.getAnnotation(Domain.class);
                pathBuilder
                        .addTag(domain.title().isEmpty() ? domain.name() : domain.title())
                        .produces(actionDefinition.produces());

            } else {
                pathBuilder
                        .addTag(actionDefinition.domain())
                        .produces(actionDefinition.produces());

            }

            pathBuilder.build();

            registry.put(new Registration(actionDefinition.method().name(), actionDefinition.path()), actionClass);

        }

        this.swagger = builder.build();
    }

    static class Registry extends AbstractMap<Registration, ActionClass> {

        private Set<Entry<Registration, ActionClass>> entrySet = new HashSet<>();

        @Override
        public Set<Entry<Registration, ActionClass>> entrySet() {
            return entrySet;
        }

        public ActionClass get(Registration registration) {
            Iterator<Entry<Registration, ActionClass>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<Registration, ActionClass> e = i.next();
                if (registration.equals(e.getKey()))
                    return e.getValue();
            }
            return null;
        }

        @Override
        public ActionClass put(Registration key, ActionClass value) {
            Map.Entry<Registration, ActionClass> entry = new SimpleEntry<>(key, value);
            entrySet.add(entry);
            return entry.getValue();
        }

        ActionCallable create(ActionRequestEvent event) throws Exception {
            ActionClass actionClass = get(event.registration);
            Class<? extends ActionCallable> actionType = actionClass.getActionType();
            ActionCallable action = actionType.newInstance();
            HttpServletRequest httpServletRequest = (HttpServletRequest) event.getSource();

            Field[] fields = actionClass.getActionFields();
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
}
