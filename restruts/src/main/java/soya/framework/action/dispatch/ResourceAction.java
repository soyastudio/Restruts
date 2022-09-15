package soya.framework.action.dispatch;

import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "resource",
        path = "/dispatch/resource",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Resource",
        description = "Extract resource through resource uri.")
public class ResourceAction extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String uri;

    @Override
    public String execute() throws Exception {
        return Resources.getResourceAsString(uri);
    }
}
