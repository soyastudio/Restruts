package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.commons.util.CodeBuilder;

import java.net.URI;

@ActionDefinition(
        domain = "reflect",
        name = "api-details",
        path = "/api/details",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "API Details",
        description = "Print action api details in markdown format."
)
public class ApiDetailsAction extends ApiAction {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "a",
            description = {
                    "ActionName in URI format, examples are:",
                    "- class://<action_class_name>",
                    "- <domain_name>://<action_name>"
            }
    )
    protected String action;

    @Override
    public String execute() throws Exception {
        URI u = URI.create(action);
        ActionClass actionClass;
        if ("class".equals(u.getScheme())) {
            actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(u.getHost()));

        } else {
            actionClass = ActionClass.get(ActionName.fromURI(u));

        }

        CodeBuilder builder = CodeBuilder.newInstance();
        render(actionClass, builder);

        return builder.toString();
    }
}
