package soya.framework.action.orchestration.pipeline;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;

public abstract class PipelineAdminAction<T> extends Action<T> {

    PipelineContainer container() {
        return ActionContext.getInstance().getService(PipelineContainer.class);
    }
}
