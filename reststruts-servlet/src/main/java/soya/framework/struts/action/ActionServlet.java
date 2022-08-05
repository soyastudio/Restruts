package soya.framework.struts.action;

import soya.framework.struts.api.Swagger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionServlet extends HttpServlet {
    private static final String SPRING_ROOT_ATTRIBUTE = "org.springframework.web.context.WebApplicationContext.ROOT";

    private Swagger swagger;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();

        if (ActionContext.getInstance() == null) {
            if (servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE) != null) {
                new SpringActionContext(servletContext.getAttribute(SPRING_ROOT_ATTRIBUTE));
            }
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("swagger.json");
        this.swagger = Swagger.fromJson(inputStream);
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
        System.out.println("============ " + req.getInputStream().getClass().getName());

        Action<?> action = create(new ActionEvent(req));
        AsyncContext asyncContext = req.startAsync();
        asyncContext.start(() -> {
            try {
                Object result = action.execute();

            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            } finally {
                asyncContext.complete();
            }
        });
    }

    private Action<?> create(ActionEvent event) {
        return new Action<Object>() {
            @Override
            public Object execute() throws Exception {
                throw new RuntimeException("xxx");
            }
        };
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
