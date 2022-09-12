package soya.framework.albertsons.actions.iib;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.PayloadMapping;

@OperationMapping(domain = "albertsons",
        name = "update-iib-application",
        path = "/workshop/iib/application",
        method = OperationMapping.HttpMethod.PUT,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Create IIB Application",
        description = "Create IIB Application based on template.")
public class ApplicationUpdateAction extends IIBDevAction<String> {

    @PayloadMapping(description = "text for encoding", consumes = MediaType.APPLICATION_JSON)
    private String bod;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
