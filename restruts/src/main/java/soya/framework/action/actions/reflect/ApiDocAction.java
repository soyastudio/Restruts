package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;

@ActionDefinition(
        domain = "reflect",
        name = "api-doc",
        path = "/api/doc",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API",
        description = "Print action apis in markdown format."
)
public class ApiDocAction extends ApiAction {

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();

        for (String dm : ActionClass.domains()) {
            Domain domain = ActionClass.domainType(dm).getAnnotation(Domain.class);
            builder.append("# DOMAIN: ").appendLine(domain.title().isEmpty() ? domain.name() : domain.title());
            builder.appendLine(domain.description());

            builder.appendLine();

            for (ActionName actionName : ActionClass.actions(dm)) {
                ActionClass actionClass = ActionClass.get(actionName);
                render(actionClass, builder);
                builder.appendLine();
            }
        }

        return builder.toString();
    }
}
