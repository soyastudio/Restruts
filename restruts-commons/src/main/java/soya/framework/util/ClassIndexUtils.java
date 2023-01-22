package soya.framework.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class ClassIndexUtils {
    private static IndexedClassStore STORE;

    static {
        STORE = IndexedClassStore.getInstance();
    }

    private ClassIndexUtils() {
    }

    public static String[] indexes() {
        List<String> list = new ArrayList<>(STORE.getIndexes());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationType) {
        return STORE.getTypesWithAnnotation(annotationType);
    }
}
