package soya.framework.albertsons.actions.transform;

import soya.framework.action.ActionProperty;

public abstract class Converter extends TransformAction {

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD,
            description = "Text for converting",
            required = true)
    protected String message;
}
