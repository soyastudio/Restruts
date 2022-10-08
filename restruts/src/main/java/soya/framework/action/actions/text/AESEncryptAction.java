package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "text-util",
        name = "aes-encrypt",
        path = "/aes-encrypt",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "AES Encrypt",
        description = "AES Encrypt")
public class AESEncryptAction extends AESAction {

    @Override
    public String execute() throws Exception {
        return encrypt(text, secret);
    }
}
