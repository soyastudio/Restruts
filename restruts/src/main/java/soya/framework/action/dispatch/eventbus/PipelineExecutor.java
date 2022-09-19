package soya.framework.action.dispatch.eventbus;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.ActionDispatch;

import java.util.Map;

public class PipelineExecutor {

    public static ActionResult execute(String signature, Map<String, Object> values) {
        ActionDispatch sig = ActionDispatch.fromURI(signature);
        ActionCallable action = sig.create(values, (expression, context) -> context.get(expression));
        return action.call();
    }

    public static void main(String[] args) {

    }
}
