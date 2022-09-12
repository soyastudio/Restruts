package soya.framework.albertsons.actions.transform;

import soya.framework.action.MediaType;
import soya.framework.action.PayloadMapping;

public abstract class Converter extends TransformAction {

    @PayloadMapping(description = "Text for converting", consumes = MediaType.TEXT_PLAIN)
    protected String message;
}
