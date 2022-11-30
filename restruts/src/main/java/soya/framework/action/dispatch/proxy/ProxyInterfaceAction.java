package soya.framework.action.dispatch.proxy;

import com.google.common.base.CaseFormat;
import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ActionPropertyAssignment;
import soya.framework.action.dispatch.AssignmentType;
import soya.framework.action.dispatch.ParamName;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Field;

@ActionDefinition(domain = "dispatch",
        name = "proxy-interface-generator",
        path = "/proxy/interface",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyInterfaceAction extends ProxyInterfaceGenerator {

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "d")
    private String configuration;

    @Override
    public String execute() throws Exception {

        return null;
    }

    private void printMethod(ActionClass actionClass, CodeBuilder builder) {
        ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);
        Field[] fields = actionClass.getActionFields();

        String methodName = definition.name().replaceAll("-", "_").toUpperCase();
        methodName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, methodName);

        builder.append("@", 1).append(ActionDispatchPattern.class.getSimpleName()).append("(").appendLine();
        builder.append("uri = \"", 3).append(actionClass.getActionName().toString()).append("\"")
                .appendLine(fields.length > 0 ? "," : "");

        if (fields.length > 0) {
            builder.appendLine("propertyAssignments = {", 3);

            for (int i = 0; i < fields.length; i++) {
                builder.append("@", 5).append(ActionPropertyAssignment.class.getSimpleName()).appendLine("(")
                        .append("name = \"", 7).append(fields[i].getName()).appendLine("\",")
                        .appendLine("assignmentType = AssignmentType.PARAMETER,", 7)
                        .append("expression = \"", 7).append(fields[i].getName()).appendLine("\"");

                builder.appendLine(")", 5);
            }

            builder.appendLine("}", 3);
        }

        builder.appendLine(")", 1);

        if (fields.length == 0) {
            builder.append("Object ", 1).append(methodName).append("(").appendLine(")");

        } else {
            builder.append("Object ", 1).append(methodName).appendLine("(");

            int index = 1;
            for (Field field : fields) {
                String paramType = getType(field.getType());
                builder.append("@", 3).append(ParamName.class.getSimpleName()).append("(\"").append(field.getName()).append("\") ")
                        .append(paramType).append(" ").append(field.getName());

                if (index < fields.length) {
                    builder.appendLine(",");

                } else {
                    builder.appendLine();

                }
                index++;
            }
            builder.appendLine(");", 1);
        }
        builder.appendLine();

    }

    private String getType(Class<?> type) {
        if (type.isPrimitive()) {
            return type.getName();
        } else if ("java.lang".equals(type.getPackage().getName())) {
            return type.getSimpleName();
        } else {
            return type.getName();
        }
    }


}