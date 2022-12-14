package soya.framework.albertsons.actions.transform;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;

import java.util.Base64;

@ActionDefinition(domain = "albertsons",
        name = "base64-decode",
        path = "/workshop/transform/base64-decode",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class Base64DecodeAction extends Converter {

    @Override
    public String execute() throws Exception {
        return new String(Base64.getDecoder().decode(message));
    }
}
