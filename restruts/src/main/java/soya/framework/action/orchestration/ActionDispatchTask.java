package soya.framework.action.orchestration;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.Assignment;
import soya.framework.action.dispatch.DefaultEvaluator;
import soya.framework.action.dispatch.Evaluator;

public final class ActionDispatchTask<T> implements Task<T> {
    private ActionDispatch actionDispatch;

    public ActionDispatchTask(ActionDispatch actionDispatch) {
        this.actionDispatch = actionDispatch;
    }

    @Override
    public T execute(ProcessSession session) {
        return (T) actionDispatch.dispatch(session).get();
    }

    private static class ParameterEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            ProcessSession session = (ProcessSession) context;
            return session.parameterValue(assignment.getExpression());
        }
    }

    private static class ReferenceEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            ProcessSession session = (ProcessSession) context;
            return session.get(assignment.getExpression());
        }
    }
}
