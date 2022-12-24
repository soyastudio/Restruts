package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.commons.util.URIUtils;

import java.lang.reflect.Field;

public abstract class ApiAction extends Action<String> {

    protected ActionRegistrationService registrationService() {
        return ActionContext.getInstance().getActionRegistrationService();
    }

    protected void render(ActionClass actionClass, CodeBuilder builder) {

        Class<? extends ActionCallable> cls = actionClass.getActionType();
        ActionDefinition desc = cls.getAnnotation(ActionDefinition.class);

        ActionDomain domain = registrationService().domain(desc.domain());
        String domainPath = domain.getPath();

        builder.append("## ACTION: ").appendLine(desc.displayName().isEmpty() ? desc.name() : desc.displayName());
        builder.appendLine(URIUtils.merge(desc.description(), "\n"));

        builder.appendLine("### 1. Action Definition");
        builder.append("- class: ").appendLine(actionClass.getActionType().getName());
        builder.append("- domain: ").appendLine(desc.domain());
        builder.append("- name: ").appendLine(desc.name());
        builder.append("- path: ").appendLine(domainPath + desc.path());
        builder.append("- http method: ").appendLine(desc.method().name());
        builder.append("- produce: ").appendLine(desc.produces()[0]);
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
                    builder.append("-- Property Type: ").appendLine(field.getType().getName());
                    builder.append("-- Parameter Type: ").appendLine(actionProperty.parameterType().name());
                    builder.append("-- Required: ").appendLine("" + actionProperty.required());
                    builder.append("-- Default Value: ").appendLine(actionProperty.defaultValue().isEmpty() ? "" : actionProperty.defaultValue());
                    builder.appendLine("-- Description: ");

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
        builder.append(desc.domain()).append("://").append(desc.name());
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
