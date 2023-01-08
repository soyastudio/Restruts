package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Field;
import java.net.URI;

@ActionDefinition(domain = "dispatch",
        name = "action-dispatch-uri",
        path = "/action-dispatch-uri",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DispatchURIGenerateAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            option = "a")

    private String actionName;

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();
        ActionClass actionClass = ActionClass.get(ActionName.fromURI(URI.create(actionName)));
        builder.append(actionClass.getActionName().toString());
        Field[] fields = actionClass.getActionFields();
        if(fields.length > 0) {
            builder.append("?");
            for (int i = 0; i < fields.length; i ++) {
                Field field = fields[i];
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                if(i != 0) {
                    builder.append("&");
                }
                builder.append(field.getName()).append("=").append(AssignmentType.PARAMETER.toString(field.getName()));
            }
        }
        return builder.toString();
    }
}
