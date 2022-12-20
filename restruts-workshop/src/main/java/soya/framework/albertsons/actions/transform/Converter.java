package soya.framework.albertsons.actions.transform;

import soya.framework.action.ActionProperty;
import soya.framework.action.ActionParameterType;

public abstract class Converter extends TransformAction {

    @ActionProperty(parameterType = ActionParameterType.PAYLOAD,
            description = "Text for converting",
            required = true)
    protected String message;
}
