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

    private Map<String, Assignment> assignments = new HashMap<>();

    public CommandDispatcher(Class<?> executeClass, String methodName) {
        super(executeClass, methodName, new Class[0]);
    }

    public CommandDispatcher assignProperty(String propName, AssignmentType assignmentType, String expression) {
        assignments.put(propName, new Assignment(assignmentType, expression));
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
        for (Map.Entry<String, Assignment> e : assignments.entrySet()) {
            String propName = e.getKey();

            Object value = null;
            Assignment assignment = e.getValue();
            if (assignment.getAssignmentType().equals(AssignmentType.VALUE)) {
                value = assignment.getExpression();

            } else if (assignment.getAssignmentType().equals(AssignmentType.RESOURCE)) {
                value = Resources.getResourceAsString(assignment.getExpression());

            } else if (assignment.getAssignmentType().equals(AssignmentType.PARAMETER)) {
                Field actionField = actionClass.getActionField(assignment.getExpression());
                actionField.setAccessible(true);
                value = actionField.get(context);

            } else if (assignment.getAssignmentType().equals(AssignmentType.REFERENCE)) {
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
