package soya.framework.action.actions.text;

import soya.framework.action.Action;
import soya.framework.action.ActionProperty;

import java.nio.charset.Charset;


public abstract class TextUtilAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            defaultValue = "utf-8",
            option = "c",
            description = "Encoding, default is utf-8."
    )
    protected String encoding = Charset.defaultCharset().toString();

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "t",
            description = "Text for processing."
    )
    protected String text;

}
