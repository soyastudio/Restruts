package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatch;

import java.net.URI;

@ActionDefinition(domain = "reflect",
        name = "signature",
        path = "/signature",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Action Signature",
        description = "Action Signature Template.")
public class SignatureAction extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String actionName;

    @Override
    public String execute() throws Exception {
        URI uri = URI.create(actionName);
        ActionName name = ActionName.create(uri.getScheme(), uri.getHost());

        ActionClass actionClass = name.getDomain().equals("class")?
                ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()))
                : ActionContext.getInstance().getActionMappings().actionClass(name);

        return actionClass.toURI();
    }
}
