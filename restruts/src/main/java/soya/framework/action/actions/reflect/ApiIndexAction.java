package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.common.util.CodeBuilder;

@ActionDefinition(domain = "reflect",
        name = "api",
        path = "/api",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API Index",
        description = "Print action apis index in yaml format.")
public class ApiIndexAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        ActionMappings mappings = ActionContext.getInstance().getActionMappings();
        CodeBuilder builder = CodeBuilder.newInstance();

        for (String dm : mappings.domains()) {
            Domain domain = mappings.domainType(dm).getAnnotation(Domain.class);
            builder.append(domain.name()).appendLine(":");

            for (ActionName actionName : mappings.actions(dm)) {
                ActionClass actionClass = mappings.actionClass(actionName);
                Class<? extends ActionCallable> cls = actionClass.getActionType();

                String name = actionClass.getActionName().getName();
                int spaces = (60 - name.length());
                if(spaces > 0) {
                    builder.append("- ", 1).append(name).appendToken(' ', spaces).append("# class://").appendLine(cls.getName());

                } else {
                    builder.append("- ", 1).append(name).append("# class://", 2).appendLine(cls.getName());
                }
            }

            builder.appendLine();
        }

        return builder.toString();
    }
}
