package soya.framework.restruts.service;

import soya.framework.action.actions.reflect.AboutAction;
import soya.framework.action.patterns.ActionMapping;
import soya.framework.action.patterns.ActionParameter;
import soya.framework.action.patterns.ActionParameterSetting;
import soya.framework.action.patterns.ActionProxy;
import soya.framework.albertsons.actions.transform.Base64DecodeAction;
import soya.framework.albertsons.actions.transform.Base64EncodeAction;

@ActionProxy
public interface Workshop {

    @ActionMapping(actionType = AboutAction.class)
    String about();

    @ActionMapping(actionType = Base64EncodeAction.class, parameterSettings = {
            @ActionParameterSetting(name = "message", value = "Marry had a little lamb.")
    })
    String base64Encode();

    @ActionMapping(actionType = Base64DecodeAction.class)
    String base64Decode(@ActionParameter("message") String msg);

}
