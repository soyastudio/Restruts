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

        for (ActionDomain domain : ActionDomain.domains()) {
            builder.append("# DOMAIN: ").appendLine(domain.getTitle());
            builder.appendLine(domain.getDescription());

            builder.appendLine();

            for (ActionName actionName : ActionClass.actions(domain.getName())) {
                ActionClass actionClass = ActionClass.get(actionName);
                render(actionClass, builder);
                builder.appendLine();
            }
        }

        return builder.toString();
    }
}
