package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "text-util",
        name = "aes-decrypt",
        path = "/aes-decrypt",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "AES Decrypt",
        description = "AES Decrypt")
public class AESDecryptAction extends AESAction {

    @Override
    public String execute() throws Exception {
        return decrypt(text, secret);
    }
}
