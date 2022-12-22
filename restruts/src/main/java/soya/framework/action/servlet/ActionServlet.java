package soya.framework.action.servlet;

import org.reflections.Reflections;
import soya.framework.action.*;
import soya.framework.action.servlet.api.Swagger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class ActionServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ActionServlet.class.getName());
    public static final String INIT_PARAM_STREAM_HANDLER = "soya.framework.action.STREAM_HANDLER";

    private ServletRegistration registration;
    private ActionRegistrationService registrationService;

    private ActionMappings actionMappings;
    private Swagger swagger;

    private Map<String, StreamWriter> readers = new HashMap<>();
    private Map<String, StreamWriter> writers = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.registration = config.getServletContext().getServletRegistration(getServletName());
        this.registrationService = ActionContext.getInstance().getActionRegistrationService();

        initStreamHandlers(config);
        initActionMappings(registrationService);
    }

    private void initActionMappings(ActionRegistrationService registrationService) {
        this.actionMappings = new ActionMappings(registrationService);
        initSwagger(actionMappings);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().equals("/swagger.json")) {
            if(actionMappings.getLastUpdateTime() > swagger.getCreatedTime()) {
                initSwagger(actionMappings);
            }

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
        AsyncContext asyncContext = req.startAsync();
        asyncContext.start(() -> {
            try {
                ActionMapping mapping = actionMappings.getActionMapping(req);
                ActionCallable action = actionMappings.create(req);
                ActionResult actionResult = action.call();

                write(actionResult.get(), mapping.getProduce(), resp);

            } catch (Exception e) {
                logger.severe(e.getMessage());
                throw new RuntimeException(e);

            } finally {
                asyncContext.complete();

            }
        });
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

    protected void initSwagger(ActionMappings mappings) {
        logger.info("initializing swagger");

        Swagger.SwaggerBuilder builder = Swagger.builder();
        String path = this.getServletInfo();
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

                pathBuilder.description(am.getDescription());
                pathBuilder.addTag(dm.getTitle() != null && !dm.getTitle().isEmpty()? dm.getTitle() : dm.getName());
                pathBuilder.produces( am.getProduce());

                if (pathBuilder != null) {
                    for (ParameterMapping pm : am.getParameters()) {
                        String name = pm.getName();
                        ActionParameterType paramType = pm.getParameterType();

                        if (ActionParameterType.PATH_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "path", pm.getDescription()).build();

                        } else if (ActionParameterType.QUERY_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "query", pm.getDescription()).build();

                        } else if (ActionParameterType.HEADER_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "header", pm.getDescription()).build();

                        } else if (ActionParameterType.COOKIE_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "cookie", pm.getDescription()).build();

                        } else if (ActionParameterType.PAYLOAD.equals(paramType)) {
                            pathBuilder.bodyParameterBuilder(name, pm.getDescription())
                                    .build()
                                    .consumes(pm.getContentType());
                        }
                    }
                }
                pathBuilder.build();
            });
        });

        this.swagger = builder.build();
    }
}
