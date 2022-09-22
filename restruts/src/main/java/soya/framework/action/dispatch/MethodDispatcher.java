package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MethodDispatcher extends Dispatcher {

    private Assignment[] parameterAssignments;

    public MethodDispatcher(Class<?> executeClass, String methodName, Class<?>[] parameterTypes) {
        super(executeClass, methodName, parameterTypes);
        this.parameterAssignments = new Assignment[parameterTypes.length];
    }

    public MethodDispatcher assignParameter(int paramIndex, AssignmentMethod assignmentMethod, String expression) {
        parameterAssignments[paramIndex] = new Assignment(assignmentMethod, expression);
        return this;
    }

    public Object dispatch(ActionCallable context) throws Exception {
        Method method = method();

        Object[] paramValues = new Object[parameterTypes.length];
        ActionClass actionClass = ActionClass.get(context.getClass());
        for (int i = 0; i < parameterTypes.length; i++) {
            Assignment assignment = parameterAssignments[i];

            Object value = null;
            if (assignment.getAssignmentMethod().equals(AssignmentMethod.VALUE)) {
                value = assignment.getExpression();

            } else if (assignment.getAssignmentMethod().equals(AssignmentMethod.RESOURCE)) {
                value = Resources.getResourceAsString(assignment.getExpression());

            } else if (assignment.getAssignmentMethod().equals(AssignmentMethod.PARAMETER)) {
                Field actionField = actionClass.getActionField(assignment.getExpression());
                actionField.setAccessible(true);
                value = actionField.get(context);

            } else if (assignment.getAssignmentMethod().equals(AssignmentMethod.REFERENCE)) {
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
