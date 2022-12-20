package soya.framework.albertsons.actions.iib;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "albertsons",
        name = "init-iib-application",
        path = "/workshop/iib/application",
        method = ActionDefinition.HttpMethod.PUT,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Initialize IIB Application",
        description = "Initialize IIB Application based on template and bod.json settings.")
public class ApplicationUpdateAction extends IIBDevAction<String> {

    @ActionProperty(parameterType = ActionParameterType.PAYLOAD,
            description = "",
            contentType = MediaType.APPLICATION_JSON,
            required = true)
    private String bod;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
