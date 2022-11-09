package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MethodDispatcher extends Dispatcher {

    private Evaluation[] parameterEvaluations;

    public MethodDispatcher(Class<?> executeClass, String methodName, Class<?>[] parameterTypes) {
        super(executeClass, methodName, parameterTypes);
        this.parameterEvaluations = new Evaluation[parameterTypes.length];
    }

    public MethodDispatcher assignParameter(int paramIndex, EvaluationMethod evaluationMethod, String expression) {
        parameterEvaluations[paramIndex] = new Evaluation(evaluationMethod, expression);
        return this;
    }

    public Object dispatch(ActionCallable context) throws Exception {
        Method method = method();

        Object[] paramValues = new Object[parameterTypes.length];
        ActionClass actionClass = ActionClass.get(context.getClass());
        for (int i = 0; i < parameterTypes.length; i++) {
            Evaluation evaluation = parameterEvaluations[i];

            Object value = null;
            if (evaluation.getAssignmentMethod().equals(EvaluationMethod.VALUE)) {
                value = evaluation.getExpression();

            } else if (evaluation.getAssignmentMethod().equals(EvaluationMethod.RESOURCE)) {
                value = Resources.getResourceAsString(evaluation.getExpression());

            } else if (evaluation.getAssignmentMethod().equals(EvaluationMethod.PARAMETER)) {
                Field actionField = actionClass.getActionField(evaluation.getExpression());
                actionField.setAccessible(true);
                value = actionField.get(context);

            } else if (evaluation.getAssignmentMethod().equals(EvaluationMethod.REFERENCE)) {
                throw new IllegalArgumentException("");
            }

            if (value != null) {
                paramValues[i] = ConvertUtils.convert(value, parameterTypes[i]);
            }
        }

        Object executor = ActionContext.getInstance().getService(executeClass);

        return method.invoke(executor, paramValues);
    }

}
