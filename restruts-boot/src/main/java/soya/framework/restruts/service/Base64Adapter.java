package soya.framework.restruts.service;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.PayloadMapping;
import soya.framework.action.patterns.ActionAdapterAction;
import soya.framework.action.patterns.ActionMapping;
import soya.framework.action.patterns.ActionParameter;
import soya.framework.albertsons.actions.transform.Base64EncodeAction;

@ActionDefinition(domain = "pattern",
        name = "encode-adaptor",
        path = "/pattern/transform/base64-encode",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@ActionMapping(actionType = Base64EncodeAction.class)
public class Base64Adapter extends ActionAdapterAction<String> {

    @PayloadMapping(description = "Text for converting", consumes = MediaType.TEXT_PLAIN)
    @ActionParameter("message")
    private String text;

}
