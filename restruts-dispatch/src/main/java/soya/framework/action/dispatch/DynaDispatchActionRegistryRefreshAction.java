package soya.framework.action.dispatch;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "dynamic-dispatch-action-registry-refresh",
        path = "/dynamic-dispatch-action/refresh",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class DynaDispatchActionRegistryRefreshAction extends DynaDispatchActionRegistryAction<Long> {

    @Override
    public Long execute() throws Exception {
        registry.touch();
        return registry.lastUpdatedTime();
    }
}
