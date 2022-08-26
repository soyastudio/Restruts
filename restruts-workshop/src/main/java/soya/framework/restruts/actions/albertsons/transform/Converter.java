package soya.framework.restruts.actions.albertsons.transform;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.PayloadMapping;

public abstract class Converter extends TransformAction {

    @PayloadMapping(description = "Text for converting", consumes = MediaType.TEXT_PLAIN)
    protected String message;
}
