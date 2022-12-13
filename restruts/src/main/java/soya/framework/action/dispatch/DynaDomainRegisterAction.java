package soya.framework.action.dispatch;

import soya.framework.action.*;

@ActionDefinition(
        domain = "dispatch",
        name = "dynamic-domain-create",
        path = "/dynamic/domain",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Dynamic Domain Registration",
        description = "Dynamic Domain Registration"
)
public class DynaDomainRegisterAction extends Action<DynaDomain> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "n",
            required = true,
            description = {}
    )
    private String name;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "p",
            description = {}
    )
    private String path;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "t",
            description = {}
    )
    private String title;

    @ActionProperty(
            parameterType = ParameterType.PAYLOAD,
            option = "d",
            description = {}
    )
    private String description;

    @WiredService
    protected DynaDomainRegistry registry;

    @Override
    public DynaDomain execute() throws Exception {
        return registry.register(name, path, title, description);
    }


}
