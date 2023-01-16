package soya.framework.convert;

public class ClassConverter implements Converter<Class<?>> {

    @Override
    public Class<?> convert(Class<Class<?>> type, Object value) throws ConvertException {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();

        if(classLoader == null) {
            classLoader = ClassConverter.class.getClassLoader();
        }

        try {
            return type.cast(classLoader.loadClass(value.toString()));

        } catch (ClassNotFoundException e) {
            throw new ConvertException(type, value);
        }
    }
}
