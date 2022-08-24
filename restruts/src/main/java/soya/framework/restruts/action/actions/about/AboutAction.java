package soya.framework.restruts.action.actions.about;

import soya.framework.commons.util.CodeBuilder;
import soya.framework.restruts.action.*;

import java.lang.reflect.Field;

@OperationMapping(domain = "about",
        name = "about",
        path = "/about",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "About",
        description = "Print as markdown format.")
public class AboutAction implements Action<String> {

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
                Class<? extends Action> cls = mappings.actionType(actionName);
                OperationMapping operation = cls.getAnnotation(OperationMapping.class);
                builder.append("## ACTION: ").appendLine(operation.displayName().isEmpty() ? operation.name() : operation.displayName());
                builder.appendLine(operation.description());

                builder.appendLine("### 1. Action Definition");
                builder.append("- domain: ").appendLine(operation.domain());
                builder.append("- name: ").appendLine(operation.name());
                builder.append("- path: ").appendLine(operation.path());
                builder.append("- http method: ").appendLine(operation.method().name());

                Field[] fields = mappings.parameterFields(cls);
                builder.appendLine("### 2. Action Parameters");
                if(fields.length == 0) {
                    builder.appendLine("No annotated parameter field.");
                } else {

                    for (Field field : fields) {
                        builder.append("- ").appendLine(field.getName());
                        if(field.getAnnotation(ParameterMapping.class) != null) {
                            ParameterMapping parameterMapping = field.getAnnotation(ParameterMapping.class);
                            builder.append("-- Description: ").appendLine(parameterMapping.description());
                            builder.append("-- Required: ").appendLine("" + parameterMapping.required());
                            builder.append("-- Java Type: ").appendLine(field.getType().getName());
                            builder.append("-- HTTP Type: ").appendLine(parameterMapping.parameterType().name());

                        } else if(field.getAnnotation(PayloadMapping.class) != null) {

                        }

                    }

                }


                builder.appendLine();
            }
        }

        return builder.toString();
    }
}
