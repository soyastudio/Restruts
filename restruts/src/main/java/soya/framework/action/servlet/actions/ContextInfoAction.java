package soya.framework.action.servlet.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@ActionDefinition(
        domain = "web",
        name = "servlet-context",
        path = "/servlet-context",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ContextInfoAction extends ServletContextAction<String> {


    @Override
    public String execute() throws Exception {
        return GSON.toJson(new ServletContextInfo(servletContext));
    }

    static class ServletContextInfo {
        private String name;
        private String contextPath;
        private int majorVersion;
        private int minorVersion;
        private int sessionTimeout;


        private Map<String, String> initParams = new HashMap<>();
        private Map<String, String> filters = new HashMap<>();
        private Map<String, String> servlets = new HashMap<>();

        private Map<String, String> attributes = new HashMap<>();

        private ServletContextInfo(ServletContext ctx) {
            this.name = ctx.getServletContextName();
            this.contextPath = ctx.getContextPath();
            this.majorVersion = ctx.getMajorVersion();
            this.minorVersion = ctx.getMinorVersion();
            this.sessionTimeout = ctx.getSessionTimeout();

            Enumeration<String> enumeration = ctx.getInitParameterNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                initParams.put(key, ctx.getInitParameter(key));
            }

            enumeration = ctx.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                attributes.put(key, ctx.getAttribute(key).getClass().getName());
            }

            ctx.getFilterRegistrations().entrySet().forEach(e -> {
                filters.put(e.getKey(), e.getValue().getClassName());
            });

            ctx.getServletRegistrations().entrySet().forEach(e -> {
                servlets.put(e.getKey(), e.getValue().getClassName());
            });

        }

    }
}
