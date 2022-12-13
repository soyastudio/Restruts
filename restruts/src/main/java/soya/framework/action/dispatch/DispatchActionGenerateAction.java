package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.action.dispatch.*;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Field;
import java.net.URI;

@ActionDefinition(domain = "dispatch",
        name = "action-dispatch-generate",
        path = "/action-dispatch/generate",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DispatchActionGenerateAction extends Action<String> {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "c")
    private String className;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "a")
    private String actionName;

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            required = true,
            option = "d")
    private String dispatch;

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();

        String packageName = className.substring(0, className.lastIndexOf('.'));
        String simpleName = className.substring(packageName.length() + 1);

        ActionName name = ActionName.fromURI(URI.create(actionName));

        ActionDispatch actionDispatch = ActionDispatch.fromURI(dispatch);
        ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());
        ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);

        builder.append("package ").append(packageName).appendLine(";");
        builder.appendLine();

        builder.append("import ").append(ActionDefinition.class.getName()).appendLine(";");
        builder.append("import ").append(ActionProperty.class.getName()).appendLine(";");
        builder.append("import ").append(MediaType.class.getName()).appendLine(";");
        builder.append("import ").append(ActionDispatchAction.class.getName()).appendLine(";");
        builder.append("import ").append(ActionDispatchPattern.class.getName()).appendLine(";");
        builder.appendLine();

        builder.append("@").append(ActionDefinition.class.getSimpleName()).appendLine("(");
        builder.append("domain = \"", 2).append(name.getDomain()).appendLine("\", ");
        builder.append("name = \"", 2).append(name.getName()).appendLine("\", ");
        builder.append("path = \"/", 2).append(name.getName()).appendLine("\", ");
        builder.append("method = ActionDefinition.HttpMethod.", 2).append(definition.method().toString()).appendLine(",");
        builder.append("produces = MediaType.", 2).append(definition.produces()[0].toString()).appendLine(",");
        builder.appendLine("description = {}", 2);
        builder.appendLine(")");
        builder.append("@").append(ActionDispatchPattern.class.getSimpleName()).appendLine("(");
        builder.append("uri = \"", 2).append(dispatch).appendLine("\"");
        builder.appendLine(")");
        builder.append("public class ").append(simpleName).append(" extends ").append(ActionDispatchAction.class.getSimpleName()).appendLine(" {");

        for (Field field : actionClass.getActionFields()) {
            Assignment assignment = actionDispatch.getAssignment(field.getName());
            if(assignment != null && assignment.getAssignmentType().equals(AssignmentType.PARAMETER)) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);

                builder.append("@", 1).append(ActionProperty.class.getSimpleName()).appendLine("(");
                builder.append("parameterType = ", 3).append("PropertyType.").append(actionProperty.parameterType().toString()).appendLine(",");
                builder.append("required = ", 3).append("" + actionProperty.required()).appendLine(",");
                builder.append("option = \"", 3).append(actionProperty.option()).appendLine("\",");
                builder.append("description = {}", 3).appendLine(",");
                builder.appendLine(")", 1);
                builder.append("private ", 1).append(getType(field.getType())).append(" ").append(assignment.getExpression()).appendLine(";");
                builder.appendLine();
            }
        }

        builder.appendLine("}");

        return builder.toString();
    }


    protected String getType(Class<?> type) {
        if (type.isPrimitive()) {
            return type.getName();
        } else if ("java.lang".equals(type.getPackage().getName())) {
            return type.getSimpleName();
        } else {
            return type.getName();
        }
    }
}
