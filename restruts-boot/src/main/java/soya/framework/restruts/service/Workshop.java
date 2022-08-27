package soya.framework.restruts.service;

import soya.framework.restruts.action.actions.about.AboutAction;
import soya.framework.restruts.action.proxy.ActionMethod;
import soya.framework.restruts.action.proxy.ActionParameter;
import soya.framework.restruts.action.proxy.ActionParameterSetting;
import soya.framework.restruts.action.proxy.ActionProxy;
import soya.framework.restruts.actions.albertsons.transform.Base64DecodeAction;
import soya.framework.restruts.actions.albertsons.transform.Base64EncodeAction;

@ActionProxy
public interface Workshop {

    @ActionMethod(actionType = AboutAction.class)
    String about();

    @ActionMethod(actionType = Base64EncodeAction.class, parameterSettings = {
            @ActionParameterSetting(name = "message", value = "Marry had a little lamb.")
    })
    String base64Encode();

    @ActionMethod(actionType = Base64DecodeAction.class)
    String base64Decode(@ActionParameter("message") String msg);

}
