package soya.framework.action.orchestration.actions;

import soya.framework.action.Domain;

@Domain(
        name = "orchestration",
        path = "/action/orchestration",
        title = "Orchestration",
        description = "Dispatch domain includes different kinds of patterns to dispatch, invoke or integrate actions at runtime."
)
public interface OrchestrationActionDomain {
}
