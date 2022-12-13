package soya.framework.action.dispatch.proxy;

import com.google.common.base.CaseFormat;
import soya.framework.action.Action;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;
import soya.framework.action.dispatch.*;
import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.commons.util.CodeBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public abstract class ProxyInterfaceGenerator extends Action<String> {

    protected void printPackage(String packageName, CodeBuilder builder) {

        builder.append("package ").append(packageName).appendLine(";");
        builder.appendLine();
    }

    protected void printImports(CodeBuilder builder) {

        builder.append("import ").append(getType(ActionDispatchPattern.class)).appendLine(";");
        builder.append("import ").append(getType(ParamName.class)).appendLine(";");
        builder.append("import ").append(getType(ActionProxyPattern.class)).appendLine(";");
        builder.appendLine();
    }

    protected void printInterfaceStart(String name, CodeBuilder builder) {
        builder.appendLine("@ActionProxyPattern");
        builder.append("public interface ").append(name).appendLine(" {");
    }

    protected void printInterfaceEnd(CodeBuilder builder) {
        builder.append("}");
    }

    protected void printMethod(String uri, CodeBuilder builder) {

        ActionDispatch actionDispatch = ActionDispatch.fromURI(uri);
        ActionClass actionClass = ActionClass.get(actionDispatch.getActionName());

        String methodName = actionDispatch.getActionName().getName().replaceAll("-", "_").toLowerCase();
        methodName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, methodName);

        builder.append("@", 1).append(ActionDispatchPattern.class.getSimpleName()).appendLine("(");
        builder.append("uri = \"", 3).append(uri).appendLine(")");
        builder.appendLine(")", 1);
        builder.append("Object ", 1).append(methodName).append("(");

        int len = actionDispatch.getParameterNames().length;

        if(len == 0) {
            builder.append(");");
        } else {
            builder.appendLine();
            int count = 0;

            for(Field field: actionClass.getActionFields()) {
                Assignment assignment = actionDispatch.getAssignment(field.getName());

                if(assignment != null && assignment.getAssignmentType().equals(AssignmentType.PARAMETER)) {
                    count ++;
                    builder.append("@", 3).append(ParamName.class.getSimpleName()).append("(\"").append(assignment.getExpression()).append("\") ");
                    builder.append(getType(field.getType())).append(" ").append(assignment.getExpression());
                    if(count < len) {
                        builder.append(",");
                    }
                    builder.appendLine();

                }
            }

            builder.appendLine(");", 1);
        }

        builder.appendLine();

    }

    protected void printMethod(ActionClass actionClass, CodeBuilder builder) {
        ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);
        Field[] fields = actionClass.getActionFields();

        String methodName = definition.name().replaceAll("-", "_").toUpperCase();
        methodName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, methodName);

        builder.append("@", 1).append(ActionDispatchPattern.class.getSimpleName()).append("(").appendLine();
        builder.append("uri = \"", 3).append(actionClass.getActionName().toString());

        if (fields.length > 0) {
            builder.append("?");

            for (int i = 0; i < fields.length; i++) {
                if(i > 0) {
                    builder.append("&");
                }
                builder.append(fields[i].getName()).append("=").append("param(").append(fields[i].getName()).append(")");
            }
        }

        builder.appendLine("\")");

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

    protected void printMethod(Method method, CodeBuilder builder) {

        Parameter[] parameters = method.getParameters();
        ActionDispatchPattern pattern = method.getAnnotation(ActionDispatchPattern.class);

        builder.append("@", 1).append(ActionDispatchPattern.class.getSimpleName()).append("(").appendLine();
        builder.append("uri = \"", 3).append(pattern.uri()).appendLine("\"");
        builder.appendLine(")", 1);

        if(parameters.length == 0) {
            builder.append(getType(method.getReturnType()), 1).append(" ").append(method.getName()).appendLine("();");

        } else {
            builder.append(getType(method.getReturnType()), 1).append(" ").append(method.getName()).appendLine("(");
            for(int i = 0; i < parameters.length; i ++) {
                Parameter parameter = parameters[i];
                String paramName = parameter.getAnnotation(ParamName.class).value();
                builder.append("@ParamName(\"", 3).append(paramName).append("\") ").append(getType(parameter.getType())).append(" ").append(paramName);
                if(i < parameters.length - 1) {
                    builder.appendLine(",");
                } else {
                    builder.appendLine();
                }
            }

            builder.appendLine(");", 1);

        }
        builder.appendLine();

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

    protected static class ProxyInterface {
        private String packageName;
        private String className;

    }

    protected static class ProxyMethod {
        private String methodName;
        private String returnType;
        private String dispatch;
        private List<MethodParameter> parameters = new ArrayList<>();

    }

    protected static class MethodParameter {
        private String name;
        private String type;
        private AssignmentType assignmentType;
        private String assignmentExpression;
    }
}
