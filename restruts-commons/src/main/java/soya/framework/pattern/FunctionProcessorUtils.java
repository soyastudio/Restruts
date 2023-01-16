package soya.framework.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public final class FunctionProcessorUtils {

    private FunctionProcessorUtils() {
    }

    public static void validate(Class<? extends FunctionalFilter> clazz) throws FunctionalFilterClassException {
        if(!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
            Constructor[] constructors = clazz.getConstructors();
            if(constructors.length != 1) {
                throw new FunctionalFilterClassException(clazz, "function processor can have only one constructor.");
            }

            if(clazz.getAnnotation(Named.class) == null) {
                throw new FunctionalFilterClassException(clazz, "function processor must be annotated as '" + Named.class.getName() + "'");
            }
        }
    }

    public static <T extends FunctionalFilter> T newInstance(String parameter, Class<T> clazz) {
        validate(clazz);
        Constructor constructor = clazz.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] args = parse(parameter, parameters);

        constructor.setAccessible(true);
        try {
            return (T) constructor.newInstance(args);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FunctionalFilterException(e);
        }
    }

    private static Object[] parse(String parameter, Parameter[] parameters) {
        Object[] args = new Object[parameters.length];

        return args;
    }

}
