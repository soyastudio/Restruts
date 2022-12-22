package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;

@ActionDefinition(
        domain = "reflect",
        name = "api",
        path = "/api",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API Index",
        description = "Print action apis index in yaml format."
)
public class ApiIndexAction extends ApiAction {

    @Override
    public String execute() throws Exception {
        CodeBuilder builder = CodeBuilder.newInstance();
        builder.appendLine("# ========== ACTION_CLASS ==========");

        for (ActionDomain domain : registrationService().domains()) {
            builder.append(domain.getName()).appendLine(":");

            for (ActionName actionName : registrationService().actions()) {
                if(actionName.getDomain().equals(domain.getName())) {
                    ActionDescription actionDescription = registrationService().action(actionName);

                    String name = actionName.getName();
                    int spaces = (60 - name.length());
                    if (spaces > 0) {
                        builder.append("- ", 1).append(name).appendToken(' ', spaces).append("# ").appendLine(actionDescription.getImplementation());

                    } else {
                        builder.append("- ", 1).append(name).append("# ", 2).appendLine(actionDescription.getImplementation());
                    }
                }
            }

            builder.appendLine();
        }

        for(String registry: registrationService().registers()) {
            builder.append("# ========== ").append(registry).appendLine(" ==========");
        }

        return builder.toString();
    }
}
