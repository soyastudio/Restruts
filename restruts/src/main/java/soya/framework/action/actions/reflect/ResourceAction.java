package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(domain = "reflect",
        name = "resource",
        path = "/resource",
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
