package soya.framework.reststruts.actions;

import soya.framework.struts.action.Action;
import soya.framework.struts.action.OperationMapping;
import soya.framework.struts.action.MediaType;

@OperationMapping(api = "Encoding",
        path = "/encoding/{xxx}/base64-encoding",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class Base64EncodeAction implements Action<String> {

    private String text;

    @Override
    public String execute() throws Exception {
        return "Hello World";
    }
}
