package soya.framework.albertsons.actions.transform;

import soya.framework.action.ActionProperty;
import soya.framework.action.ParameterType;

public abstract class Converter extends TransformAction {

    @ActionProperty(parameterType = ParameterType.PAYLOAD,
            description = "Text for converting",
            required = true)
    protected String message;
}
