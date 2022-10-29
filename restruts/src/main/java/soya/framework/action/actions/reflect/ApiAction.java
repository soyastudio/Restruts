package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.commons.util.StringUtils;

import java.lang.reflect.Field;

public abstract class ApiAction extends Action<String> {

    protected void render(ActionClass actionClass, CodeBuilder builder) {

        Class<? extends ActionCallable> cls = actionClass.getActionType();
        ActionDefinition operation = cls.getAnnotation(ActionDefinition.class);

        Class<?> domainType = ActionContext.getInstance().getActionMappings().domainType(operation.domain());
        String domainPath = domainType == null ? "" : domainType.getAnnotation(Domain.class).path();

        builder.append("## ACTION: ").appendLine(operation.displayName().isEmpty() ? operation.name() : operation.displayName());
        builder.appendLine(StringUtils.merge(operation.description(), "\n"));

        builder.appendLine("### 1. Action Definition");
        builder.append("- class: ").appendLine(actionClass.getActionType().getName());
        builder.append("- domain: ").appendLine(operation.domain());
        builder.append("- name: ").appendLine(operation.name());
        builder.append("- path: ").appendLine(domainPath + operation.path());
        builder.append("- http method: ").appendLine(operation.method().name());
        builder.append("- produce: ").appendLine(operation.produces()[0]);
        builder.appendLine();

        builder.appendLine("### 2. Action Properties");
        Field[] fields = actionClass.getActionFields();
        if (fields.length == 0) {
            builder.appendLine("No annotated field.");

        } else {
            for (Field field : fields) {
                builder.append("- ").appendLine(field.getName());
                if (field.getAnnotation(ActionProperty.class) != null) {
                    ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                    builder.append("-- Description: ").appendLine(StringUtils.merge(actionProperty.description(), "\n"));
                    builder.append("-- Property Type: ").appendLine(field.getType().getName());
                    builder.append("-- HTTP Input Type: ").appendLine(actionProperty.parameterType().name());
                    builder.append("-- Required: ").appendLine("" + actionProperty.required());
                    builder.append("-- Default Value: ").appendLine(actionProperty.defaultValue().isEmpty() ? "" : actionProperty.defaultValue());

                }

            }

        }
        builder.appendLine();

        builder.appendLine("### 3. ActionExecutor DSL");
        builder.appendLine("```");
        builder.append("Object result = ", 2).appendLine(ActionExecutor.class.getName());
        builder.append(".executor(", 4).append(cls.getName()).appendLine(".class)");
        for (Field field : fields) {
            if (field.getAnnotation(ActionProperty.class) != null) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                builder.append(".setProperty(\"", 4).append(field.getName()).append("\", ")
                        .append("<").append(field.getName()).append(">")
                        .appendLine(")");
            }

        }

        builder.appendLine(".execute();", 4);
        builder.appendLine();
        builder.appendLine("```");
        builder.appendLine();


        builder.appendLine("### 4. Commandline");
        builder.appendLine("```");
        builder.append(operation.domain()).append("://").append(operation.name());
        for (Field field : fields) {
            if (field.getAnnotation(ActionProperty.class) != null) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                if (actionProperty.option().isEmpty()) {
                    builder.append(" --").append(field.getName()).append(" <").append(field.getName()).append(">");

                } else {
                    builder.append(" -").append(actionProperty.option()).append(" <").append(field.getName()).append(">");

                }
            }

        }
        builder.appendLine();
        builder.appendLine("```");
        builder.appendLine();

        builder.appendLine("### 5. Action Dispatch URI");
        builder.appendLine("```");
        builder.appendLine(actionClass.toURI());
        builder.appendLine();
        builder.appendLine("```");

        builder.appendLine("Here assign() should be one of val(), res(), param() or ref()");
        builder.appendLine();
    }
}
