package soya.framework.albertsons.actions.iib;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.PayloadMapping;

@ActionDefinition(domain = "albertsons",
        name = "init-iib-application",
        path = "/workshop/iib/application",
        method = ActionDefinition.HttpMethod.PUT,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Initialize IIB Application",
        description = "Initialize IIB Application based on template and bod.json settings.")
public class ApplicationUpdateAction extends IIBDevAction<String> {

    @PayloadMapping(description = "text for encoding", consumes = MediaType.APPLICATION_JSON)
    private String bod;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
