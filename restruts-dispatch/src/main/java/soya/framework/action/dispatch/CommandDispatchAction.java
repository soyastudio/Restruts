package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionResult;

public abstract class CommandDispatchAction<T> extends Action<T> {

    @Override
    public final T execute() throws Exception {
        CommandDispatchPattern annotation = getClass().getAnnotation(CommandDispatchPattern.class);
        CommandDispatcher dispatcher = new CommandDispatcher(annotation.commandType(), annotation.methodName());
        for (ActionPropertyAssignment assignment : annotation.propertyAssignments()) {
            dispatcher.assignProperty(assignment.name(), assignment.assignmentType(), assignment.expression());
        }

        return convert(dispatcher.dispatch(this));
    }

    protected T convert(Object methodResult) {
        if (methodResult instanceof ActionResult) {
            return (T) ((ActionResult) methodResult).get();
        }

        return (T) methodResult;
    }
}
