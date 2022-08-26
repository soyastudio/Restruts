package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.PayloadMapping;

import java.util.Base64;

@OperationMapping(domain = "albertsons",
        name = "base64-decode",
        path = "/workshop/transform/base64-decode",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class Base64DecodeAction extends Converter {

    @Override
    public String execute() throws Exception {
        return new String(Base64.getDecoder().decode(message));
    }
}
