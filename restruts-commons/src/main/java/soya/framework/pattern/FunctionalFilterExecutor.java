package soya.framework.pattern;

import soya.framework.annotation.Named;
import soya.framework.util.ClassIndexUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class FunctionalFilterExecutor {
    private static Factory FACTORY;

    private final Constructor constructor;
    private final Object[] parameterValues;

    private FunctionalFilterExecutor(Constructor constructor, Object[] parameterValues) {
        this.constructor = constructor;
        this.parameterValues = parameterValues;
    }

    public <T> T execute(T data) throws FunctionalFilterException {
        try {
            return ((FunctionalFilter<T>) constructor.newInstance(parameterValues)).process(data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FunctionalFilterException(e);
        }
    }

    public static <T> T execute(String expression, T data) throws FunctionalFilterException {
        FunctionalFilter<T> filter = (FunctionalFilter<T>) builder(expression).create();
        return filter.process(data);
    }

    private static Factory getFactory() {
        if (FACTORY == null) {
            new DefaultFactory();
        }
        return FACTORY;
    }

    public static Builder builder(String expression) {
        FunctionExpression func = FunctionExpression.parse(expression);
        Builder builder = new Builder(getFactory().get(func.getName()).getDeclaringClass());
        if (func.getParameters().length > 0) {
            if (func.getParameters().length != builder.getParameterCount()) {
                throw new IllegalArgumentException("Function parameter count does not match: " + expression);
            } else {
                // TODO: set param values
            }
        }

        return builder;
    }

    public static Builder builder(Class<? extends FunctionalFilter> type) {
        return new Builder(type);
    }

    public static class Builder {
        private Class<? extends FunctionalFilter> type;
        private Constructor constructor;
        private Object[] parameterValues;
        private Map<String, Integer> parameterNames = new HashMap<>();

        private int cursor;

        private Builder(Class<? extends FunctionalFilter> type) {
            this.type = type;
            this.constructor = type.getConstructors()[0];
            this.parameterValues = new Object[constructor.getParameterCount()];
        }

        public int getParameterCount() {
            return constructor.getParameterCount();
        }

        public int getCursor() {
            return cursor;
        }

        public Builder addParameter(Object value) {
            parameterValues[cursor] = value;
            cursor++;
            return this;
        }

        public Builder setParameter(int index, Object value) {
            parameterValues[index] = value;
            return this;
        }

        public Builder setParameterName(int index, String name) {
            parameterNames.put(name, index);
            return this;
        }

        public FunctionalFilter<?> create() {
            try {
                return (FunctionalFilter<?>) constructor.newInstance(parameterValues);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new FunctionalFilterClassException(type, e.getMessage());
            }
        }
    }

    public static abstract class Factory {

        protected Map<String, Constructor> constructors = new LinkedHashMap<>();

        protected Factory() {
            if (FACTORY != null) {
                throw new IllegalStateException("Factory instance already exist.");
            }

            init();
            FACTORY = this;
        }

        protected Constructor get(String functionName) {
            if (!constructors.containsKey(functionName)) {
                throw new IllegalArgumentException("Function is not defined: " + functionName);
            }
            return constructors.get(functionName);
        }

        protected abstract void init();
    }

    static class DefaultFactory extends Factory {
        protected DefaultFactory() {
            super();
        }

        @Override
        protected void init() {
            ClassIndexUtils
                    .getAnnotatedClasses(Named.class)
                    .stream()
                    .filter(c -> FunctionalFilter.class.isAssignableFrom(c))
                    .collect(Collectors.toList()).forEach(e -> {
                        FunctionProcessorUtils.validate((Class<? extends FunctionalFilter>) e);
                        constructors.put(e.getAnnotation(Named.class).value(), e.getConstructors()[0]);
                    });
        }
    }

}
