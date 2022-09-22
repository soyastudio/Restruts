package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.net.URI;

@ActionDefinition(domain = "dispatch",
        name = "action-dispatch-template",
        path = "/dispatch/action",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ActionDispatchTemplate extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String actionName;

    @Override
    public String execute() throws Exception {
        ActionClass actionClass = null;
        URI uri = URI.create(actionName);
        if(uri.getScheme().equals("class")) {
            actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()));

        } else {
            actionClass = ActionContext.getInstance().getActionMappings().actionClass(ActionName.fromURI(uri));
        }

        if(actionClass == null) {
            throw new IllegalArgumentException("");
        }

        return actionClass.toURI();
    }
}
