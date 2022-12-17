package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "dynamic-domain-create",
        path = "/dynamic-domain",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DynaDomainCreateAction extends Action<Boolean> {

    @Override
    public Boolean execute() throws Exception {
        return null;
    }
}
