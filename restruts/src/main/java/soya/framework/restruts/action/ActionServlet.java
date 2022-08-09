package soya.framework.restruts.action;

import org.reflections.Reflections;
import soya.framework.restruts.api.Swagger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

public class ActionServlet extends HttpServlet {

    public static final String INIT_PARAM_SCAN_PACKAGES = "soya.framework.restruts.SCAN_ACTION_PACKAGES";
    public static final String INIT_PARAM_SERIALIZER = "soya.framework.restruts.SERIALIZER";
    public static final String INIT_PARAM_DESERIALIZER = "soya.framework.restruts.DESERIALIZER";

    public static final String SPRING_ROOT_ATTRIBUTE = "org.springframework.web.context.WebApplicationContext.ROOT";

    private ActionContext actionContext;
    private DefaultActionMappings actionMappings;
    private Swagger swagger;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();

        if (ActionContext.getInstance() == null) {
             this.actionMappings = new DefaultActionMappings();

            String scanPackages = config.getInitParameter(INIT_PARAM_SCAN_PACKAGES);
            if (scanPackages != null) {
                actionMappings.scan(scanPackages.split(","));
            }

            if (servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE) != null) {
                this.actionContext = new SpringActionContext(actionMappings, servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE));
            }
        }

        this.swagger = actionMappings.toSwagger();
        String path = this.getServletInfo();
        ServletRegistration registration = config.getServletContext().getServletRegistration(getServletName());
        for (String e : registration.getMappings()) {
            if (e.endsWith("/*")) {
                path = e.substring(0, e.lastIndexOf("/*"));
            }
        }
        swagger.setBasePath(path);
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
        if (!actionMappings.containsKey(event.registration)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            try {
                Action action = actionMappings.create(event);
                final String contentType = contentType(req.getHeader("Accept"),
                        actionMappings.get(event.registration).getAnnotation(OperationMapping.class).produces());

                AsyncContext asyncContext = req.startAsync();
                asyncContext.start(() -> {
                    try {
                        Object result = action.execute();
                        ServletIoSupport.write(result, contentType, resp.getOutputStream());

                    } catch (Exception e) {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                    } finally {
                        asyncContext.complete();
                    }
                });

            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private String contentType(String accept, String[] produces) {
        if (accept != null) {
            String token = accept.indexOf(";") > 0 ? accept.substring(0, accept.indexOf(";")) : accept;
            String[] accepts = token.split(",");
            for (String acc : accepts) {
                for (String p : produces) {
                    if (acc.equalsIgnoreCase(p)) {
                        return acc;
                    }
                }
            }

        }

        return produces[0];
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

    static class DefaultActionMappings extends AbstractMap<Registration, Class<? extends Action>> implements ActionMappings {

        private Map<String, Class<?>> apis = new HashMap<>();
        private Set<Entry<Registration, Class<? extends Action>>> entrySet = new HashSet<>();

        @Override
        public Set<Entry<Registration, Class<? extends Action>>> entrySet() {
            return entrySet;
        }

        public Class<? extends Action> get(Registration registration) {
            Iterator<Entry<Registration, Class<? extends Action>>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<Registration, Class<? extends Action>> e = i.next();
                if (registration.equals(e.getKey()))
                    return e.getValue();
            }
            return null;
        }

        @Override
        public Class<? extends Action> put(Registration key, Class<? extends Action> value) {
            Map.Entry<Registration, Class<? extends Action>> entry = new SimpleEntry<>(key, value);
            entrySet.add(entry);
            return entry.getValue();
        }

        void scan(String... pkgs) {
            for (String pkg : pkgs) {
                Reflections reflections = new Reflections(pkg.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(API.class);
                set.forEach(c -> {
                    API api = c.getAnnotation(API.class);
                    apis.put(api.value(), c);
                });
            }

            for (String pkg : pkgs) {
                Reflections reflections = new Reflections(pkg.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(OperationMapping.class);
                set.forEach(c -> {
                    OperationMapping operationMapping = c.getAnnotation(OperationMapping.class);
                    Registration registration = new Registration(operationMapping.method().name(), operationMapping.path());
                    if (Action.class.isAssignableFrom(c)) {
                        put(registration, (Class<? extends Action>) c);
                    }
                });
            }
        }

        public Swagger toSwagger() {
            entrySet.forEach(e -> {
                OperationMapping operationMapping = e.getValue().getAnnotation(OperationMapping.class);
            });

            Swagger.SwaggerBuilder builder = Swagger.builder();

            List<String> tags = new ArrayList<>(apis.keySet());
            Collections.sort(tags);
            tags.forEach(e -> {
                API api = apis.get(e).getAnnotation(API.class);
                builder.addTag(Swagger.TagObject.instance().name(api.value()).description(api.description()));
            });

            List<Registration> registrations = new ArrayList<>(keySet());
            Collections.sort(registrations);

            registrations.forEach(e -> {
                Class<? extends Action> cls = get(e);
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

                if (pathBuilder != null) {
                    for (Field f : paramFields(cls)) {
                        if (f.getAnnotation(ParameterMapping.class) != null) {
                            ParameterMapping param = f.getAnnotation(ParameterMapping.class);
                            String name = param.name().isEmpty() ? f.getName() : param.name();
                            pathBuilder.parameterBuilder(name, param.parameterType().name().toLowerCase(), param.description()).build();

                        } else if (f.getAnnotation(PayloadMapping.class) != null) {
                            PayloadMapping payload = f.getAnnotation(PayloadMapping.class);
                            String name = payload.name().isEmpty() ? f.getName() : payload.name();
                            pathBuilder.bodyParameterBuilder(name, payload.description())
                                    .build()
                                    .consumes(payload.consumes());

                        }
                    }

                }

                pathBuilder
                        .addTag(operationMapping.api())
                        .produces(operationMapping.produces());


                pathBuilder.build();

            });

            return builder.build();
        }

        private List<Field> paramFields(Class<? extends Action> actionType) {
            List<Field> fields = new ArrayList<>();
            Set<String> fieldNames = new HashSet<>();
            Class<?> cls = actionType;
            while (!cls.getName().equals("java.lang.Object")) {
                for (Field field : cls.getDeclaredFields()) {
                    if (field.getAnnotation(ParameterMapping.class) != null
                            || field.getAnnotation(PayloadMapping.class) != null
                            || !fieldNames.contains(field.getName())) {

                        fields.add(field);
                        fieldNames.add(field.getName());
                    }
                }
                cls = cls.getSuperclass();
            }

            Collections.sort(fields, new ParameterFieldComparator());
            return fields;
        }

        @Override
        public Class<? extends Action> getActionType(HttpServletRequest request) {
            ActionRequestEvent event = new ActionRequestEvent(request);
            return get(event.registration);
        }

        Action<?> create(ActionRequestEvent event) throws Exception {
            Class<? extends Action> actionType = get(event.registration);
            Action<?> action = get(event.registration).newInstance();
            HttpServletRequest httpServletRequest = (HttpServletRequest) event.getSource();

            List<Field> fields = paramFields(actionType);
            fields.forEach(e -> {
                Class<?> paramType = e.getType();
                Object value = null;
                if (e.getAnnotation(PayloadMapping.class) != null) {
                    if (paramType.equals(String.class)) {
                        value = event.getPayload(paramType);
                    }

                } else if (e.getAnnotation(ParameterMapping.class) != null) {
                    ParameterMapping parameterMapping = e.getAnnotation(ParameterMapping.class);
                    String name = parameterMapping.name() == null ? parameterMapping.name() : e.getName();
                    String paramValue = null;
                    if(ParameterMapping.ParameterType.HEADER_PARAM.equals(parameterMapping.parameterType())) {
                        paramValue = httpServletRequest.getHeader(name);

                    } else if (ParameterMapping.ParameterType.QUERY_PARAM.equals(parameterMapping.parameterType())) {
                        paramValue = event.queryParams.get(name).get(0);

                    } else if (ParameterMapping.ParameterType.QUERY_PARAM.equals(parameterMapping.parameterType())) {

                    }
                }

                if (value != null) {
                    e.setAccessible(true);
                    try {
                        e.set(action, value);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            return action;
        }
    }

    static class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            if (o1.getAnnotation(PayloadMapping.class) != null) {
                return 1;
            } else if (o2.getAnnotation(PayloadMapping.class) != null) {
                return -1;
            }

            if (o1.getAnnotation(ParameterMapping.class) != null && o2.getAnnotation(ParameterMapping.class) != null) {
                int result = ParameterMapping.ParameterType.index(o1.getAnnotation(ParameterMapping.class).parameterType())
                        - ParameterMapping.ParameterType.index(o2.getAnnotation(ParameterMapping.class).parameterType());
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
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
                inputData = new byte[request.getContentLength()];

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                while ((nRead = inputStream.read(inputData, 0, inputData.length)) != -1) {
                    buffer.write(inputData, 0, nRead);
                }
            } catch (IOException exception) {

            }
        }

        public String getHeader(String name) {
            HttpServletRequest request = (HttpServletRequest) getSource();
            return  request.getHeader(name);
        }

        public String getQueryParameter(String name) {
            if(queryParams.containsKey(name)) {
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

    static class SpringActionContext extends ActionContext {
        private static final Method GET_SERVICE_BY_TYPE;
        private static final Method GET_SERVICE_BY_NAME;

        private Object applicationContext;

        static {
            try {
                Class<?> INTERFACE = Class.forName("org.springframework.beans.factory.BeanFactory");
                GET_SERVICE_BY_TYPE = INTERFACE.getDeclaredMethod("getBean", new Class[]{Class.class});
                GET_SERVICE_BY_NAME = INTERFACE.getDeclaredMethod("getBean", new Class[]{String.class, Class.class});

            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        SpringActionContext(ActionMappings actionMappings, Object applicationContext) {
            super(actionMappings);
            this.applicationContext = applicationContext;
        }

        @Override
        public <T> T getService(Class<T> type) {
            try {
                return (T) GET_SERVICE_BY_TYPE.invoke(applicationContext, new Object[]{type});

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T getService(String name, Class<T> type) {
            try {
                return (T) GET_SERVICE_BY_NAME.invoke(applicationContext, new Object[]{name, type});

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
