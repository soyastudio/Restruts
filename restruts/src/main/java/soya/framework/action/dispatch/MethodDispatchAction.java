package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.action.dispatch.AssignmentType;
import soya.framework.action.dispatch.MethodDispatchPattern;
import soya.framework.action.dispatch.MethodParameterAssignment;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class MethodDispatchAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        ActionClass actionClass = ActionClass.get(getClass());
        MethodDispatchPattern annotation = getClass().getAnnotation(MethodDispatchPattern.class);

        Class<?> cls = annotation.type();
        String methodName = annotation.methodName();

        MethodParameterAssignment[] assignments = annotation.parameterAssignments();
        Class<?>[] paramTypes = new Class[assignments.length];
        Object[] paramValues = new Object[paramTypes.length];

        for (int i = 0; i < assignments.length; i++) {
            paramTypes[i] = assignments[i].type();

            if (AssignmentType.VALUE.equals(assignments[i].assignmentMethod())) {
                paramValues[i] = ConvertUtils.convert(assignments[i].expression(), paramTypes[i]);

            } else if (AssignmentType.RESOURCE.equals(assignments[i].assignmentMethod())) {
                if (InputStream.class.isAssignableFrom(paramTypes[i])) {
                    paramValues[i] = Resources.getResourceAsInputStream(assignments[i].expression());
                } else {
                    paramValues[i] = ConvertUtils.convert(Resources.getResourceAsString(assignments[i].expression()), paramTypes[i]);
                }

            } else if (AssignmentType.REFERENCE.equals(assignments[i].assignmentMethod())) {
                paramValues[i] = ActionContext.getInstance().getService(assignments[i].expression(), assignments[i].type());

            } else if (AssignmentType.PARAMETER.equals(assignments[i].assignmentMethod())) {
                Field field = actionClass.getActionField(assignments[i].expression());
                field.setAccessible(true);
                paramValues[i] = field.get(this);
            }
        }

        Method method = cls.getMethod(methodName, paramTypes);
        Object instance = Modifier.isStatic(method.getModifiers()) ? null : ActionContext.getInstance().getService(cls);


        return convert(method.invoke(instance, paramValues));
    }

    protected T convert(Object methodResult) {
        return (T) methodResult;
    }
}
