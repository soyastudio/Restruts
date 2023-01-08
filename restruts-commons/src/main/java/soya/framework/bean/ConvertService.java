package soya.framework.bean;

public abstract class ConvertService {
    private static ConvertService INSTANCE;

    protected ConvertService() {
        INSTANCE = this;
    }

    protected static ConvertService getInstance() {
        if(INSTANCE == null) {
            new DefaultConvertService();
        }
        return INSTANCE;
    }

    protected abstract Object execute(Object value, Class<?> clazz);

    public static Object convert(final Object value, final Class<?> clazz) {
        return getInstance().execute(value, clazz);
    }

    public static class DefaultConvertService extends ConvertService {
        protected DefaultConvertService() {
            super();
        }

        @Override
        protected Object execute(Object value, Class<?> clazz) {
            if(clazz.isInstance(value)) {
                return value;
            }
            return null;
        }
    }
}
