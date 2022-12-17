package soya.framework.commons.util;

import org.reflections.Reflections;
import soya.framework.action.dispatch.FragmentFunction;
import soya.framework.action.dispatch.FragmentProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReflectUtils {

    private ReflectUtils() {
    }

    public static <T> Set<Class<? extends T>> scanForSubType(String packageName , Class<T> type) {
        return new Reflections(packageName).getSubTypesOf(type);
    }

    public static Set<Class<?>> scanForAnnotation(Class<? extends Annotation> type) {
        return new Reflections().getTypesAnnotatedWith(type);
    }

    public static Set<Class<?>> scanForAnnotation(String packageName , Class<? extends Annotation> type) {
        return new Reflections(packageName).getTypesAnnotatedWith(type);
    }

    public static Field findField(Class<?> cls, String fieldName) {
        Class<?> parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            try {
                Field field = parent.getDeclaredField(fieldName);
                if (field != null) {
                    return field;
                }
            } catch (NoSuchFieldException e) {

            }
            parent = parent.getSuperclass();
        }

        throw new IllegalArgumentException("Cannot find field '" + fieldName + "' for class: " + cls.getName());
    }

    public static Method[] findMethods(Class<?> cls, String methodName) {
        List<Method> methodList = new ArrayList<>();
        Class parent = cls;
        while (parent != null) {
            for (Method method : parent.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    methodList.add(method);
                }
            }

            parent = cls.getSuperclass();
        }

        return methodList.toArray(new Method[methodList.size()]);
    }

    public static Method findMethod(Class<?> cls, String methodName) {
        Class parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            List<Method> methodList = new ArrayList<>();
            for (Method method : parent.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    methodList.add(method);
                }
            }

            if (methodList.size() == 1) {
                return methodList.get(0);
            } else if (methodList.size() > 1) {
                throw new IllegalArgumentException("There are " + methodList.size() + " methods named as '" + methodName + "' for class: " + cls.getName());
            }

            parent = cls.getSuperclass();
        }

        return null;
    }


}
