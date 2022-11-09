package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ActionDefinition(domain = "reflect",
        name = "current-time",
        path = "/runtime/current-time",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Current Time",
        description = "Print current time.")
public class CurrentTimeAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "f",
            description = "Time format."

    )
    protected String format;

    @Override
    public String execute() throws Exception {
        if(format != null) {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(new Date());

        } else {
            return new Date().toString();

        }
    }
}
