package soya.framework.convert;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.*;

public abstract class ConvertService {

    private static ConvertService INSTANCE;

    static {
        new DefaultConvertService();
    }

    protected ConvertService() {
        INSTANCE = this;
    }

    protected static ConvertService getInstance() {
        return INSTANCE;
    }

    protected abstract <T> T convert(Class<T> type, Object value) throws ConvertException;

    public static <T> T convert(final Object value, final Class<T> type) throws ConvertException {
        return getInstance().convert(type, value);
    }

    public static class DefaultConvertService extends ConvertService {

        private NullConverter nullConverter;
        private StringConverter stringConverter;
        private BooleanConverter booleanConverter;
        private CharacterConverter characterConverter;
        private NumberConverter numberConverter;
        private DateTimeConverter dateTimeConverter;
        private Converter defaultConverter;

        private Map<Class<?>, Converter> converterMap = new LinkedHashMap<>();

        protected DefaultConvertService() {
            this(new Configuration());
        }

        public DefaultConvertService(Configuration configuration) {
            super();
            this.nullConverter = configuration.nullConverter;
            this.stringConverter = configuration.stringConverter;
            this.booleanConverter = configuration.booleanConverter;
            this.characterConverter = configuration.characterConverter;
            this.numberConverter = configuration.numberConverter;
            this.dateTimeConverter = configuration.dateTimeConverter;
            this.converterMap.putAll(configuration.converterMap);
            this.defaultConverter = configuration.defaultConverter;
        }

        @Override
        protected <T> T convert(Class<T> type, Object value) throws ConvertException {

            if (type.isInstance(value)) {
                return (T) value;
            }

            if (value == null) {
                return (T) numberConverter.convert(type, null);
            }

            if (type.isPrimitive()) {
                if (type == Boolean.TYPE) {
                    return (T) booleanConverter.convert(Boolean.class, value);

                } else if (type == Character.TYPE) {
                    return (T) characterConverter.convert(Character.class, value);

                } else if (type == Integer.TYPE) {
                    return (T) numberConverter.convert(Integer.class, value);

                } else if (type == Double.TYPE) {
                    return (T) numberConverter.convert(Double.class, value);

                } else if (type == Long.TYPE) {
                    return (T) numberConverter.convert(Long.class, value);

                } else if (type == Float.TYPE) {
                    return (T) numberConverter.convert(Float.class, value);

                } else if (type == Short.TYPE) {
                    return (T) numberConverter.convert(Short.class, value);

                } else if (type == Byte.TYPE) {
                    return (T) numberConverter.convert(Byte.class, value);
                }
            }

            if (String.class.equals(type)) {
                return (T) stringConverter.convert(String.class, value);
            }

            if (converterMap.containsKey(type)) {
                return (T) converterMap.get(type).convert(type, value);
            }

            if (Number.class.isAssignableFrom(type)) {
                return (T) numberConverter.convert(type, value);
            }

            if (Date.class.isAssignableFrom(type)) {
                return (T) dateTimeConverter.convert(type, value);
            }

            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (value.getClass().isArray()) {
                    int length = Array.getLength(value);
                    Object array = Array.newInstance(componentType, length);
                    for (int i = 0; i < length; i++) {
                        Object element = Array.get(value, i);
                        Array.set(array, i, convert(componentType, element));
                    }

                    return (T) array;

                } else if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    int length = collection.size();
                    Object array = Array.newInstance(componentType, length);

                    int i = 0;
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        Object element = iterator.next();
                        Array.set(array, i, convert(componentType, element));
                        i++;
                    }

                } else {
                    throw new ConvertException("");
                }
            }

            return (T) defaultConverter.convert(type, value);
        }
    }

    public static class Configuration {

        private NullConverter nullConverter = new NullConverter();
        private StringConverter stringConverter = new StringConverter();
        private BooleanConverter booleanConverter = new BooleanConverter();
        private CharacterConverter characterConverter = new CharacterConverter();
        private NumberConverter numberConverter = new NumberConverter();
        private DateTimeConverter dateTimeConverter = new DateTimeConverter();
        private Map<Class<?>, Converter> converterMap = new LinkedHashMap<>();
        private Converter defaultConverter = new GenericConverter();

        public Configuration() {
            converterMap.put(Class.class, new ClassConverter());
            converterMap.put(Calendar.class, new CalendarConverter());
            converterMap.put(File.class, new FileConverter());
            converterMap.put(URI.class, new URIConverter());
            converterMap.put(URL.class, new URLConverter());
        }

        public Configuration setNullConverter(NullConverter nullConverter) {
            this.nullConverter = nullConverter;
            return this;
        }

        public Configuration setStringConverter(StringConverter stringConverter) {
            this.stringConverter = stringConverter;
            return this;
        }

        public Configuration setBooleanConverter(BooleanConverter booleanConverter) {
            this.booleanConverter = booleanConverter;
            return this;
        }

        public Configuration setCharacterConverter(CharacterConverter characterConverter) {
            this.characterConverter = characterConverter;
            return this;
        }

        public Configuration setNumberConverter(NumberConverter numberConverter) {
            this.numberConverter = numberConverter;
            return this;
        }

        public Configuration setDateTimeConverter(DateTimeConverter dateTimeConverter) {
            this.dateTimeConverter = dateTimeConverter;
            return this;
        }

        public Configuration setDefaultConverter(Converter defaultConverter) {
            this.defaultConverter = defaultConverter;
            return this;
        }

        public <T> Configuration addConverter(Class<T> type, Converter<? extends T> converter) {
            converterMap.put(type, converter);
            return this;
        }
    }

}
