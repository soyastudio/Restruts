package soya.framework.action.dispatch;

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

public abstract class AssignmentEvaluator {

    public static AssignmentEvaluator DEFAULT_EVALUATOR = new DefaultEvaluator(new DefaultReferenceResolver(), new DefaultReferenceResolver());

    public Object evaluate(Assignment assignment, Object context, Class<?> type) {
        Object value = null;

        AssignmentType assignmentType = assignment.getAssignmentType();
        String expression = assignment.getExpression();
        if (AssignmentType.VALUE.equals(assignmentType)) {
            value = ConvertUtils.convert(expression, type);

        } else if (AssignmentType.RESOURCE.equals(assignmentType)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (AssignmentType.REFERENCE.equals(assignmentType)) {
            value = fromReference(expression, context, type);

        } else if (AssignmentType.PARAMETER.equals(assignmentType)) {
            value = fromParameter(expression, context, type);
        }

        return value;
    }

    protected abstract Object fromParameter(String exp, Object context, Class<?> type);

    protected abstract Object fromReference(String exp, Object context, Class<?> type);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Resolver parameterResolver;
        private Resolver referenceResolver;

        private Builder() {
        }

        public Builder parameterResolver(Resolver resolver) {
            this.parameterResolver = resolver;
            return this;
        }

        public Builder referenceResolver(Resolver resolver) {
            this.referenceResolver = resolver;
            return this;
        }

        public AssignmentEvaluator create() {
            return new DefaultEvaluator(parameterResolver, referenceResolver);
        }
    }

    static class DefaultEvaluator extends AssignmentEvaluator {
        private static Resolver DEFAULT_PARAMETER_RESOLVER = new DefaultParameterResolver();
        private static Resolver DEFAULT_REFERENCE_RESOLVER = new DefaultReferenceResolver();

        private Resolver parameterResolver;
        private Resolver referenceResolver;

        DefaultEvaluator(Resolver parameterResolver, Resolver referenceResolver) {
            this.parameterResolver = parameterResolver;
            this.referenceResolver = referenceResolver;
        }

        @Override
        protected Object fromParameter(String exp, Object context, Class<?> type) {
            if (parameterResolver == null) {
                return ConvertUtils.convert(DEFAULT_PARAMETER_RESOLVER.resolve(exp, context), type);
            } else {
                return ConvertUtils.convert(parameterResolver.resolve(exp, context), type);
            }

        }

        @Override
        protected Object fromReference(String exp, Object context, Class<?> type) {
            if (referenceResolver == null) {
                return ConvertUtils.convert(DEFAULT_REFERENCE_RESOLVER.resolve(exp, context), type);

            } else {
                return ConvertUtils.convert(referenceResolver.resolve(exp, context), type);

            }
        }
    }

    static class DefaultParameterResolver implements Resolver {

        @Override
        public Object resolve(String expression, Object context) {
            Object value = null;
            if (context instanceof ActionCallable) {
                Field field = ReflectUtils.findField(context.getClass(), expression);
                field.setAccessible(true);
                try {
                    value = field.get(context);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (context instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) context;
                value = map.get(expression);

            } else if (context instanceof ActionDispatchSession) {
                ActionDispatchSession session = (ActionDispatchSession) context;
                value = session.parameterValue(expression);

            } else if (context instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) context;
                value = jsonObject.get(expression);

            } else {
                try {
                    value = PropertyUtils.getProperty(context, expression);

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    static class DefaultReferenceResolver implements Resolver {

        @Override
        public Object resolve(String expression, Object context) {
            if (context instanceof ActionDispatchSession) {
                ActionDispatchSession session = (ActionDispatchSession) context;
                return resolve(expression, session.data());

            } else {
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
