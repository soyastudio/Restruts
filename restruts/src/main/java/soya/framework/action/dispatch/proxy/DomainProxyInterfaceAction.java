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
        name = "proxy-domain",
        path = "/proxy/domain",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DomainProxyInterfaceAction extends Action<String> {

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "c")
    private String className;

    @ActionProperty(
            description = {
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "d")
    private String domain;

    @Override
    public String execute() throws Exception {
        int lastPoint = className.lastIndexOf('.');
        String packageName = className.substring(0, lastPoint);
        String simpleName = className.substring(lastPoint + 1);


        CodeBuilder builder = CodeBuilder.newInstance();

        builder.append("package ").append(packageName).appendLine(";");
        builder.appendLine();

        builder.append("import ").append(getType(ActionDispatchPattern.class)).appendLine(";");
        builder.append("import ").append(getType(ActionPropertyAssignment.class)).appendLine(";");
        builder.append("import ").append(getType(AssignmentType.class)).appendLine(";");
        builder.append("import ").append(getType(ParamName.class)).appendLine(";");
        builder.append("import ").append(getType(ActionProxyPattern.class)).appendLine(";");
        builder.appendLine();

        builder.appendLine("@ActionProxyPattern");
        builder.append("public interface ").append(simpleName).appendLine(" {");

        ActionName[] actionNames = ActionContext.getInstance().getActionMappings().actions(domain);
        for (ActionName actionName : actionNames) {
            printMethod(ActionContext.getInstance().getActionMappings().actionClass(actionName), builder);
        }

        builder.append("}");

        return builder.toString();
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
