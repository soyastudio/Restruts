package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(
        domain = "reflect",
        name = "runtime-service-details",
        path = "/runtime/service-details",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Details",
        description = "Print runtime service detail information."
)
public class RuntimeServiceDetailsAction extends Action<ServiceInfo> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            option = "s",
            description = "Service name or type."
    )
    private String service;

    @Override
    public ServiceInfo execute() throws Exception {
        Object serviceInstance = null;
        try {
            serviceInstance = ActionContext.getInstance().getService(service);

        } catch (Exception e) {

        }

        if (serviceInstance == null) {
            serviceInstance = ActionContext.getInstance().getService(Class.forName(service));
        }

        return new ServiceInfo(service, serviceInstance);
    }
}
