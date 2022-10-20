package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(domain = "reflect",
        name = "service-details",
        path = "/runtime/service-details",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Details",
        description = "Print runtime service detail information.")
public class RuntimeServiceDetailsAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "s",
            description = "Service name or type."
    )
    private String service;

    @Override
    public String execute() throws Exception {
        Object serviceInstance = null;
        try {
            serviceInstance = ActionContext.getInstance().getService(service);

        } catch (Exception e) {

        }

        if(serviceInstance == null) {
            serviceInstance = ActionContext.getInstance().getService(Class.forName(service));
        }

        return serviceInstance.getClass().getName();
    }
}
