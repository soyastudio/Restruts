package soya.framework.action.dispatch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionCallable;
import soya.framework.action.ActionResult;
import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;
import soya.framework.commons.util.ReflectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DefaultEvaluator implements Evaluator {

    private Evaluator parameterEvaluator = new DefaultParameterEvaluator();
    private Evaluator referenceEvaluator = new DefaultReferenceEvaluator();

    public DefaultEvaluator() {
    }

    public DefaultEvaluator(Evaluator parameterEvaluator, Evaluator referenceEvaluator) {
        this.parameterEvaluator = parameterEvaluator;
        this.referenceEvaluator = referenceEvaluator;
    }

    @Override
    public Object evaluate(Assignment assignment, Object context, Class<?> type) {
        Object value = null;

        AssignmentMethod assignmentMethod = assignment.getAssignmentMethod();
        String expression = assignment.getExpression();
        if (AssignmentMethod.VALUE.equals(assignmentMethod)) {
            value = ConvertUtils.convert(expression, type);

        } else if (AssignmentMethod.RESOURCE.equals(assignmentMethod)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (AssignmentMethod.REFERENCE.equals(assignmentMethod)) {
            value = referenceEvaluator.evaluate(assignment, context, type);

        } else if (AssignmentMethod.PARAMETER.equals(assignmentMethod)) {
            value = parameterEvaluator.evaluate(assignment, context, type);
        }

        return value;
    }

    static class DefaultParameterEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            String exp = assignment.getExpression();
            Object value = null;
            if (context instanceof ActionCallable) {
                Field field = ReflectUtils.findField(context.getClass(), exp);
                field.setAccessible(true);
                try {
                    value = field.get(context);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (context instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) context;
                value = map.get(exp);

            } else if (context instanceof ActionDispatchSession) {
                ActionDispatchSession session = (ActionDispatchSession) context;
                value = session.parameterValue(exp);

            } else if (context instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) context;
                value = new Gson().fromJson(jsonObject.get(exp), type);

            } else {
                try {
                    value = PropertyUtils.getProperty(context, exp);

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            return ConvertUtils.convert(value, type);
        }
    }

    static class DefaultReferenceEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            if (context instanceof ActionDispatchSession) {
                ActionDispatchSession session = (ActionDispatchSession) context;
                return evaluate(assignment, session.data(), type);

            } else {
                String expression = assignment.getExpression();
                String[] arr = expression.split("\\.");
                Object value = context;
                for (String exp : arr) {
                    value = evaluate(exp, value);
                }

                return value;
            }
        }

        private Object evaluate(String exp, Object value) {

            if (value == null) {
                return null;

            } else if (value instanceof ActionResult) {
                return ((ActionResult) value).get();

            } else if (value instanceof Map) {
                return ((Map) value).get(exp);

            } else if (value instanceof JsonObject) {
                return ((JsonObject) value).get(exp);

            } else {
                try {
                    return PropertyUtils.getProperty(value, exp);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    return null;
                }
            }
        }
    }
}
