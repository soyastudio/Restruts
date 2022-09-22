package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.common.util.CodeBuilder;
import soya.framework.common.util.StringUtils;

import java.lang.reflect.Field;

@ActionDefinition(domain = "about",
        name = "about",
        path = "/about",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "About",
        description = "Print as markdown format.")
public class AboutAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        ActionMappings mappings = ActionContext.getInstance().getActionMappings();
        CodeBuilder builder = CodeBuilder.newInstance();

        for (String dm : mappings.domains()) {
            Domain domain = mappings.domainType(dm).getAnnotation(Domain.class);
            builder.append("# DOMAIN: ").appendLine(domain.title().isEmpty() ? domain.name() : domain.title());
            builder.appendLine(domain.description());

            builder.appendLine();

            for (ActionName actionName : mappings.actions(dm)) {
                ActionClass actionClass = mappings.actionClass(actionName);

                Class<? extends ActionCallable> cls = actionClass.getActionType();
                ActionDefinition operation = cls.getAnnotation(ActionDefinition.class);
                builder.append("## ACTION: ").appendLine(operation.displayName().isEmpty() ? operation.name() : operation.displayName());
                builder.appendLine(StringUtils.merge(operation.description(), "\n"));

                builder.appendLine("### 1. Action Dispatch URI");
                builder.appendLine("```");
                builder.appendLine(actionClass.toURI());
                builder.appendLine();
                builder.appendLine("```");

                builder.appendLine("Here assign() should be one of val(), res(), param() or ref()");
                builder.appendLine();

                builder.appendLine("### 2. Action Definition");
                builder.append("- class: ").appendLine(actionClass.getActionType().getName());
                builder.append("- domain: ").appendLine(operation.domain());
                builder.append("- name: ").appendLine(operation.name());
                builder.append("- path: ").appendLine(operation.path());
                builder.append("- http method: ").appendLine(operation.method().name());
                builder.append("- produce: ").appendLine(operation.produces()[0]);

                Field[] fields = actionClass.getActionFields();
                builder.appendLine("### 3. Action Properties");
                if (fields.length == 0) {
                    builder.appendLine("No annotated field.");

                } else {
                    for (Field field : fields) {
                        builder.append("- ").appendLine(field.getName());
                        if (field.getAnnotation(ActionProperty.class) != null) {
                            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                            builder.append("-- Description: ").appendLine(actionProperty.description());
                            builder.append("-- Property Type: ").appendLine(field.getType().getName());
                            builder.append("-- HTTP Input Type: ").appendLine(actionProperty.parameterType().name());
                            builder.append("-- Required: ").appendLine("" + actionProperty.required());
                            builder.append("-- Default Value: ").appendLine(actionProperty.defaultValue().isEmpty()? "" : actionProperty.defaultValue());

                        }

                    }

                }


                builder.appendLine();
            }
        }

        return builder.toString();
    }
}
