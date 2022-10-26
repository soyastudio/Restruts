package soya.framework.action.dispatch.pipeline;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.ActionDispatchSession;

public interface PipelineTask {
    ActionResult execute(ActionDispatchSession session);
}
