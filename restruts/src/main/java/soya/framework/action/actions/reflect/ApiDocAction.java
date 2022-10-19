package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.common.util.CodeBuilder;
import soya.framework.common.util.StringUtils;

import java.lang.reflect.Field;

@ActionDefinition(domain = "reflect",
        name = "api-doc",
        path = "/api",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API",
        description = "Print action apis in markdown format.")
public class ApiDocAction extends ApiAction {

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
                render(actionClass, builder);
                builder.appendLine();
            }
        }

        return builder.toString();
    }
}
