package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.*;

@ActionDefinition(domain = "reflect",
        name = "runtime-service-discovery",
        path = "/runtime-service-discovery",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Runtime Services",
        description = "Print runtime services information of special type.")

public class RuntimeServiceDiscoveryAction extends Action<ServiceInfo[]> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "t",
            description = "Service type."
    )
    private String serviceType;

    @Override
    public ServiceInfo[] execute() throws Exception {

        List<ServiceInfo> list = new ArrayList<>();
        ActionContext.getInstance().getServices(Class.forName(serviceType)).entrySet().forEach(e -> {

            Object o = e.getValue();
            ServiceInfo info = new ServiceInfo(e.getKey(), o);

            list.add(info);
        });

        return list.toArray(new ServiceInfo[list.size()]);
    }
}
