package soya.framework.util;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public abstract class IndexedClassStore {

    private static IndexedClassStore instance;

    protected static IndexedClassStore getInstance() {
        if (instance == null) {
            new DefaultIndexedClassStore();
        }

        return instance;
    }

    protected IndexedClassStore() {
        instance = this;
    }

    protected abstract Set<String> getIndexes();

    protected abstract Set<Class<?>> getTypesWithAnnotation(Class<? extends Annotation> annotationType);

    public static String[] indexes() {
        List<String> list = new ArrayList<>(getInstance().getIndexes());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationType) {
        return getInstance().getTypesWithAnnotation(annotationType);
    }

    public static class DefaultIndexedClassStore extends IndexedClassStore {
        private Reflections reflections;

        protected DefaultIndexedClassStore() {
            super();
            ConfigurationBuilder configuration = new ConfigurationBuilder()
                    .forPackage("soya.framework")
                    .filterInputsBy(new FilterBuilder().includePackage("soya.framework"))
                    .setScanners(TypesAnnotated);

            this.reflections = new Reflections(configuration);
        }

        public DefaultIndexedClassStore(String... packages) {
            super();

            ConfigurationBuilder configuration = new ConfigurationBuilder()
                    .forPackages(packages)
                    .setScanners(TypesAnnotated);

            this.reflections = new Reflections(configuration);

        }

        @Override
        protected Set<String> getIndexes() {
            Set<String> set = new HashSet<>();
            reflections.getStore().values().forEach(sub -> {
                set.addAll(sub.keySet());
            });

            return set;
        }

        @Override
        protected Set<Class<?>> getTypesWithAnnotation(Class<? extends Annotation> annotationType) {
            return reflections.getTypesAnnotatedWith(annotationType);
        }
    }


}
