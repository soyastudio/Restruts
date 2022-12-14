package soya.framework.albertsons.actions.transform;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ActionDefinition(domain = "albertsons",
        name = "base64-encode",
        path = "/workshop/transform/base64-encode",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class Base64EncodeAction extends Converter {

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }
}
