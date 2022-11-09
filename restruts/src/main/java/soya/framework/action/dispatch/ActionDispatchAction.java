package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.net.URI;

public abstract class ActionDispatchAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {

        ActionDispatchPattern actionMapping = getClass().getAnnotation(ActionDispatchPattern.class);
        ActionName actionName = ActionName.fromURI(URI.create(actionMapping.uri()));
        ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(actionName);

        Class<? extends ActionCallable> actionType = actionClass.getActionType();
        ActionCallable action = actionClass.newInstance();

        for (ActionPropertyAssignment assignment : actionMapping.propertyAssignments()) {
            EvaluationMethod evaluationMethod = assignment.assignmentMethod();
            Field field = ReflectUtils.findField(actionType, assignment.name());
            Object value = null;
            if (EvaluationMethod.VALUE.equals(evaluationMethod)) {
                value = assignment.expression();

            } else if (EvaluationMethod.RESOURCE.equals(evaluationMethod)) {
                value = Resources.getResourceAsString(assignment.expression());

            } else if (EvaluationMethod.PARAMETER.equals(evaluationMethod)) {
                Field paramField = ReflectUtils.findField(getClass(), assignment.expression());
                paramField.setAccessible(true);
                value = paramField.get(this);

            } else if (EvaluationMethod.REFERENCE.equals(evaluationMethod)) {
                throw new IllegalArgumentException("No context defined for 'REFERENCE' assignment.");

            }

            if (value != null) {
                field.setAccessible(true);
                field.set(action, ConvertUtils.convert(value, field.getType()));
            }
        }

        ActionResult result = action.call();
        if (result.success()) {
            return convert(result.get());

        } else {
            throw (Exception) result.get();
        }
    }

    protected T convert(Object result) {
        return (T) result;
    }

}
