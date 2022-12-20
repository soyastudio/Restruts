package soya.framework.action.servlet.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ActionParameterType;

import javax.servlet.FilterRegistration;
import java.util.Collection;
import java.util.Map;

@ActionDefinition(
        domain = "web",
        name = "filter-info",
        path = "/filter-info",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names."
)
public class FilterInfoAction extends ServletContextAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "n",
            required = true,
            description = {}
    )
    private String name;

    @Override
    public String execute() throws Exception {
        FilterRegistration registration = servletContext.getFilterRegistration(name);
        if(registration != null) {
            return GSON.toJson(new FilterInfo(registration));
        }

        return null;
    }

    static class FilterInfo {
        private String name;
        private String className;
        private Collection<String> servletNameMappings;
        private Collection<String> urlPatternMappings;
        private Map<String, String> initParams;

        public FilterInfo(FilterRegistration filterRegistration) {
            this.name = filterRegistration.getName();
            this.className = filterRegistration.getClassName();
            this.servletNameMappings = filterRegistration.getServletNameMappings();
            this.urlPatternMappings = filterRegistration.getUrlPatternMappings();
            this.initParams = filterRegistration.getInitParameters();
        }
    }
}
