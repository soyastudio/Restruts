package soya.framework.action.servlet.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ActionParameterType;

import javax.servlet.ServletRegistration;
import java.util.*;

@ActionDefinition(
        domain = "web",
        name = "servlet-info",
        path = "/servlet-info",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class ServletInfoAction extends ServletContextAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "n",
            required = true,
            description = {}
    )
    private String name;

    @Override
    public String execute() throws Exception {
        ServletRegistration registration = servletContext.getServletRegistration(name);
        if(registration != null) {

            return GSON.toJson(new ServletInfo(registration));
        }

        return null;
    }

    static class ServletInfo {
        private String name;
        private String className;
        private Set<String> mappings;
        private String runAsRole;
        private Map<String, String> initParams;

        public ServletInfo(ServletRegistration servletRegistration) {
            this.name = servletRegistration.getName();
            this.className = servletRegistration.getClassName();
            this.mappings = new HashSet<>(servletRegistration.getMappings());
            this.runAsRole = servletRegistration.getRunAsRole();
            this.initParams = servletRegistration.getInitParameters();
        }
    }
}
