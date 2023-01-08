package soya.framework.action.dispatch;

import soya.framework.action.Domain;

@Domain(
        name = "dispatch",
        path = "/action/dispatch",
        title = "Dispatch",
        description = "Dispatch domain includes different kinds of patterns to dispatch, invoke or integrate actions at runtime."
)
public interface DispatchActionDomain {

}
