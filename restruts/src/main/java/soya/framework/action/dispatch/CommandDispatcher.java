package soya.framework.action.dispatch;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;
import soya.framework.commons.util.ReflectUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class CommandDispatcher extends Dispatcher {

    private Map<String, Evaluation> assignments = new HashMap<>();

    public CommandDispatcher(Class<?> executeClass, String methodName) {
        super(executeClass, methodName, new Class[0]);
    }

    public CommandDispatcher assignProperty(String propName, EvaluationMethod evaluationMethod, String expression) {
        assignments.put(propName, new Evaluation(evaluationMethod, expression));
        return this;
    }

    public Object dispatch(ActionCallable context) throws Exception {
        Method method = method();
        Map<String, PropertyDescriptor> propertyDescriptorMap = new HashMap<>();
        for (PropertyDescriptor ppt : Introspector.getBeanInfo(executeClass).getPropertyDescriptors()) {
            propertyDescriptorMap.put(ppt.getName(), ppt);
        }

        Object executor = executeClass.newInstance();

        ActionClass actionClass = ActionClass.get(context.getClass());
        for (Map.Entry<String, Evaluation> e : assignments.entrySet()) {
            String propName = e.getKey();

            Object value = null;
            Evaluation evaluation = e.getValue();
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
                if (propertyDescriptorMap.containsKey(propName) && propertyDescriptorMap.get(propName).getWriteMethod() != null) {
                    PropertyDescriptor ppt = propertyDescriptorMap.get(propName);
                    ppt.getWriteMethod().invoke(executor, new Object[]{ConvertUtils.convert(value, ppt.getPropertyType())});

                } else if (ReflectUtils.findField(executeClass, propName) != null) {
                    Field field = ReflectUtils.findField(executeClass, propName);
                    field.setAccessible(true);
                    field.set(executor, ConvertUtils.convert(value, field.getType()));
                }
            }

        }

        return method.invoke(executor, new Object[0]);
    }
}
