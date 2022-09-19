package soya.framework.action.dispatch;

import soya.framework.action.Action;

public abstract class MethodDispatchAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        MethodDispatchPattern annotation = getClass().getAnnotation(MethodDispatchPattern.class);
        if(annotation.methodParameterTypes().length != annotation.propertyAssignments().length) {
            throw new IllegalArgumentException("");
        }

        MethodDispatcher dispatcher = new MethodDispatcher(annotation.type(), annotation.methodName(), annotation.methodParameterTypes());
        for(int i = 0; i < annotation.propertyAssignments().length; i ++) {
            ActionPropertyAssignment assignment = annotation.propertyAssignments()[i];
            dispatcher.assignParameter(i, assignment.assignmentMethod(), assignment.expression());
        }

        return convert(dispatcher.dispatch(this));
    }

    protected T convert(Object methodResult) {
        return (T) methodResult;
    }
}
