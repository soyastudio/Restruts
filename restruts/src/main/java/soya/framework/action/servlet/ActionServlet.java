package soya.framework.action.servlet;

import org.reflections.Reflections;
import soya.framework.action.*;
import soya.framework.action.servlet.api.Swagger;
import soya.framework.commons.util.URIUtils;

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

    private ActionMappings actionMappings;
    private Swagger swagger;

    private Registry registry = new Registry();

    private Map<String, StreamWriter> readers = new HashMap<>();
    private Map<String, StreamWriter> writers = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.actionMappings = (ActionMappings) config.getServletContext().getAttribute(ActionMappings.ACTION_MAPPINGS_ATTRIBUTE);

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
        ActionMapping mapping = actionMappings.get(req);
        System.out.println("---------------- " + mapping.getHttpMethod() + ": " + mapping.getPathMapping());

        ActionRequestEvent event = new ActionRequestEvent(req);

        if (!registry.containsKey(event.registration)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {

            ActionClass actionClass = registry.get(event.registration);

            AsyncContext asyncContext = req.startAsync();
            asyncContext.start(() -> {
                try {
                    ActionCallable action = registry.create(event);
                    ActionResult actionResult = action.call();

                    write(actionResult.get(), actionClass.getResultFormat(), resp);

                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    throw new RuntimeException(e);

                } finally {
                    asyncContext.complete();

                }
            });
        }
    }

    protected void write(Object object, String contentType, HttpServletResponse response) throws IOException {
        if (object == null) {

        } else if (object instanceof Exception) {
            printException((Exception) object, response);

        } else {
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
        ActionMappings mappings = (ActionMappings) config.getServletContext().getAttribute(ActionMappings.ACTION_MAPPINGS_ATTRIBUTE);

        Swagger.SwaggerBuilder builder = Swagger.builder();

        String path = this.getServletInfo();
        ServletRegistration registration = config.getServletContext().getServletRegistration(getServletName());
        for (String e : registration.getMappings()) {
            if (e.endsWith("/*")) {
                path = e.substring(0, e.lastIndexOf("/*"));
            }
        }
        builder.basePath(path);

        mappings.domains().forEach(dm -> {
            builder.addTag(Swagger.TagObject.instance()
                    .name(dm.getTitle().isEmpty() ? dm.getName() : dm.getTitle())
                    .description(dm.getDescription()));

            dm.getActionMappings().forEach(am -> {
                ActionName actionName = am.getActionName();
                String httpMethod = am.getHttpMethod().toUpperCase();

                String fullPath = dm.getPath() + am.getPath();

                String operationId = dm.getName().replaceAll("_", "-")
                        + "_"
                        + actionName.getName().replaceAll("_", "-");


                Swagger.PathBuilder pathBuilder = null;
                if (httpMethod.equals("GET")) {
                    pathBuilder = builder.get(fullPath, operationId);

                } else if (httpMethod.equals("POST")) {
                    pathBuilder = builder.post(fullPath, operationId);

                } else if (httpMethod.equals("DELETE")) {
                    pathBuilder = builder.delete(fullPath, operationId);

                } else if (httpMethod.equals("PUT")) {
                    pathBuilder = builder.put(fullPath, operationId);

                } else if (httpMethod.equals("HEAD")) {
                    pathBuilder = builder.head(fullPath, operationId);

                } else if (httpMethod.equals("OPTIONS")) {
                    pathBuilder = builder.options(fullPath, operationId);

                } else if (httpMethod.equals("PATCH")) {
                    pathBuilder = builder.patch(fullPath, operationId);

                }

                StringBuilder descBuilder = new StringBuilder(am.getDescription()).append("\n");
                descBuilder.append("- Action name: ").append(actionName.toString()).append("\n");
                //descBuilder.append("- Action class: ").append(.getName()).append("\n");

                pathBuilder.description(descBuilder.toString());
                pathBuilder.addTag(dm.getTitle() != null && !dm.getTitle().isEmpty()? dm.getTitle() : dm.getName());
                pathBuilder.produces( am.getProduce());

                if (pathBuilder != null) {
                    for (ParameterMapping pm : am.getParameters()) {
                        String name = pm.getName();
                        ParameterType paramType = pm.getParameterType();

                        if (ParameterType.PATH_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "path", pm.getDescription()).build();

                        } else if (ParameterType.QUERY_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "query", pm.getDescription()).build();

                        } else if (ParameterType.HEADER_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "header", pm.getDescription()).build();

                        } else if (ParameterType.COOKIE_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "cookie", pm.getDescription()).build();

                        } else if (ParameterType.PAYLOAD.equals(paramType)) {
                            pathBuilder.bodyParameterBuilder(name, pm.getDescription())
                                    .build()
                                    .consumes(pm.getContentType());
                        }
                    }
                }
                pathBuilder.build();

                registry.put(new Registration(am.getHttpMethod(), fullPath), ActionClass.get(am.getActionName()));
            });
        });


        List<Registration> registrations = new ArrayList<>(registry.keySet());
        Collections.sort(registrations);

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
            ActionCallable action = actionClass.newInstance();

            HttpServletRequest httpServletRequest = (HttpServletRequest) event.getSource();

            Field[] fields = actionClass.getActionFields();
            for (Field field : fields) {
                Class<?> paramType = field.getType();
                Object value = null;
                if (field.getAnnotation(ActionProperty.class) != null) {
                    ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                    String name = actionProperty.name().isEmpty() ? field.getName() : actionProperty.name();
                    if (ParameterType.PAYLOAD.equals(actionProperty.parameterType())) {
                        value = event.getPayload(paramType);

                    } else if (ParameterType.HEADER_PARAM.equals(actionProperty.parameterType())) {
                        value = ConvertUtils.convert(httpServletRequest.getHeader(name), field.getType());

                    } else if (ParameterType.QUERY_PARAM.equals(actionProperty.parameterType())) {
                        value = ConvertUtils.convert(event.queryParams.get(name).get(0), field.getType());

                    } else if (ParameterType.QUERY_PARAM.equals(actionProperty.parameterType())) {
                        value = ConvertUtils.convert(event.getQueryParameter(name), field.getType());

                    } else if (ParameterType.COOKIE_PARAM.equals(actionProperty.parameterType())) {
                        // TODO:
                    }
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
            int result = path.compareTo(o.path);

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
            if (inputData == null || inputData.length == 0) {
                return null;
            }

            if (type.getName().equals("java.lang.String")) {
                return (T) new String(inputData);
            }

            return null;
        }
    }
}
