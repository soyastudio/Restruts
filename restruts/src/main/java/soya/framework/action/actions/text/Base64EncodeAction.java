package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.Base64;

@ActionDefinition(domain = "text-util",
        name = "base64-encode",
        path = "/base64-encode",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Base64 Encode",
        description = "Base64 Encode")
public class Base64EncodeAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(text.getBytes(encoding));
    }

}
