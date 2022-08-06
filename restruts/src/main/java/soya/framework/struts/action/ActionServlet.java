package soya.framework.struts.action;

import org.reflections.Reflections;
import soya.framework.struts.api.Swagger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ActionServlet extends HttpServlet {

    public static final String INIT_PARAM_SCAN_PACKAGES = "soya.framework.struts.SCAN_ACTION_PACKAGES";
    public static final String INIT_PARAM_SERIALIZER = "soya.framework.struts.SERIALIZER";
    public static final String INIT_PARAM_DESERIALIZER = "soya.framework.struts.DESERIALIZER";

    public static final String SPRING_ROOT_ATTRIBUTE = "org.springframework.web.context.WebApplicationContext.ROOT";

    private Swagger swagger;
    private Mappings mappings = new Mappings();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();

        if (ActionContext.getInstance() == null) {
            if (servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE) != null) {
                new SpringActionContext(servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE));
            }
        }

        String scanPackages = config.getInitParameter(INIT_PARAM_SCAN_PACKAGES);
        if (scanPackages != null) {
            String[] pkgs = scanPackages.split(",");
            for (String pkg : pkgs) {
                Reflections reflections = new Reflections(pkg.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(API.class);
                set.forEach(c -> {
                    API api = c.getAnnotation(API.class);
                    mappings.apis.put(api.value(), c);
                });
            }

            for (String pkg : pkgs) {
                Reflections reflections = new Reflections(pkg.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(OperationMapping.class);
                set.forEach(c -> {
                    OperationMapping operationMapping = c.getAnnotation(OperationMapping.class);
                    Registration registration = new Registration(operationMapping.method().name(), operationMapping.path());
                    if (Action.class.isAssignableFrom(c)) {
                        mappings.put(registration, (Class<? extends Action>) c);
                    }
                });
            }
        }

        this.swagger = mappings.toSwagger();

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
        ActionEvent event = new ActionEvent(req);
        if (!mappings.containsKey(event.registration)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            Action action = create(event);
            final String contentType = contentType(req.getHeader("Accept"), mappings.get(event.registration).getAnnotation(OperationMapping.class).produces());

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

    private Action<?> create(ActionEvent event) throws ServletException {
        Action<?> action = null;
        try {
            action = mappings.get(event.registration).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServletException(e);
        }

        return action;
    }

    static class Registration {
        private String method;
        private String[] paths;

        Registration(String method, String paths) {
            this.method = method;
            this.paths = paths.split("/");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof Registration)) return false;

            Registration that = (Registration) o;
            if (!method.equals(that.method) || paths.length != that.paths.length) {
                return false;
            }

            for (int i = 0; i < paths.length; i++) {
                if (!paths[i].equals(that.paths[i]) && !(that.paths[i].contains("{") && that.paths[i].contains("}"))) {
                    return false;
                }
            }

            return true;
        }
    }

    static class Mappings extends AbstractMap<Registration, Class<? extends Action>> {

        private Map<String, Class<?>> apis = new HashMap<>();
        private Set<Entry<Registration, Class<? extends Action>>> entrySet = new HashSet<>();

        @Override
        public Set<Entry<Registration, Class<? extends Action>>> entrySet() {
            return entrySet;
        }

        @Override
        public Class<? extends Action> put(Registration key, Class<? extends Action> value) {
            Map.Entry<Registration, Class<? extends Action>> entry = new SimpleEntry<>(key, value);
            entrySet.add(entry);
            return entry.getValue();
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

            return builder.build();
        }
    }

    static class ActionEvent extends EventObject {
        private Registration registration;
        private Object payload;

        ActionEvent(HttpServletRequest request) {
            super(request);
            this.registration = new Registration(request.getMethod(), request.getPathInfo());
            request.getPathInfo();
        }

        public <T> T getPayload(Class<T> type) {
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

        SpringActionContext(Object applicationContext) {
            super();
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
