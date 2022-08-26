package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@OperationMapping(domain = "albertsons",
        name = "base64-encode",
        path = "/workshop/transform/base64-encode",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class Base64EncodeAction extends Converter {

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }
}
