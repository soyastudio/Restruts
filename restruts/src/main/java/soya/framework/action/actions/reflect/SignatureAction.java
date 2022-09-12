package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.net.URI;

@OperationMapping(domain = "about",
        name = "signature",
        path = "/signature",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Action Signature",
        description = "Action Signature Template.")
public class SignatureAction extends Action<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String actionName;

    @Override
    public String execute() throws Exception {
        URI uri = URI.create(actionName);
        ActionName name = ActionName.create(uri.getScheme(), uri.getHost());

        ActionClass actionClass = name.getDomain().equals("class")?
                ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()))
                : ActionContext.getInstance().getActionMappings().actionClass(name);

        return actionClass.signature().toURI();
    }
}
