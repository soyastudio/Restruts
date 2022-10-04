package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.Base64;

@ActionDefinition(domain = "text-util",
        name = "base64-decode",
        path = "/text-util/base64-decode",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Base64 Decode",
        description = "Base64 Decode")
public class Base64DecodeAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        return new String(Base64.getDecoder().decode(text.getBytes()), encoding);
    }
}
