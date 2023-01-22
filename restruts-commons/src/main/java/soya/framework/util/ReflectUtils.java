package soya.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static <T extends Annotation> T findAnnotationRecursively(Class<?> c, Class<T> a) {
        Class<?> type = Objects.requireNonNull(c);
        T annotation = c.getAnnotation(Objects.requireNonNull(a));
        while (annotation == null && !type.equals(Object.class)) {
            if(annotation != null) {
                return annotation;
            }
            type = type.getSuperclass();
            annotation = type.getAnnotation(a);
        }

        return null;
    }

    public static Set<Class<?>> findClassesOfPackage(String packageName)  {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static Field[] getFields(Class<?> cls) {
        Map<String, Field> fields = new LinkedHashMap<>();
        Class<?> parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            Arrays.stream(parent.getDeclaredFields()).forEach(e -> {
                if(!fields.containsKey(e.getName())) {
                    fields.put(e.getName(), e);
                }
            });

            parent = parent.getSuperclass();
        }

        return fields.values().toArray(new Field[fields.size()]);
    }

    public static Field[] getFieldsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationType) {
        Map<String, Field> fields = new LinkedHashMap<>();
        Class<?> parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            Arrays.stream(parent.getDeclaredFields()).forEach(e -> {
                if(!fields.containsKey(e.getName()) && isAnnotatedAs(e, annotationType)) {
                    fields.put(e.getName(), e);
                }
            });

            parent = parent.getSuperclass();
        }

        return fields.values().toArray(new Field[fields.size()]);
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

    public static boolean isAnnotatedAs(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        String annotationClassName = annotationType.getName();
        Annotation[] annotations = element.getAnnotations();
        for(Annotation annotation: annotations) {
            if(annotation.annotationType().getName().equals(annotationClassName)) {
                return true;
            }
        }

        return false;
    }

}
