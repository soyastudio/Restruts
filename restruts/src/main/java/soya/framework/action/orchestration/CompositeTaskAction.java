package soya.framework.action.orchestration;

import soya.framework.action.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public abstract class CompositeTaskAction<P extends Task<T>, T> extends Action<T> {

    private P task;

    @ActionProperty(
            parameterType = ParameterType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON
    )
    protected String data;

    @Override
    public final T execute() throws Exception {
        return (T) task.execute(newSession());
    }

    @Override
    protected void prepare() throws ActionException {
        super.prepare();
        this.task = build();
    }

    protected abstract P build();

    protected ProcessSession newSession() {
        return new Session(this);
    }

    protected static class Session implements ProcessSession {

        private final String name;
        private final String id;
        private final long createdTime;

        private Map<String, Object> parameters = new LinkedHashMap<>();
        private Map<String, Object> attributes = new HashMap<>();

        Session(ActionCallable action) {
            ActionClass actionClass = ActionClass.get(action.getClass());

            this.name = actionClass.getActionName().toString();
            this.id = UUID.randomUUID().toString();
            this.createdTime = System.currentTimeMillis();

            Field[] fields = actionClass.getActionFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    parameters.put(field.getName(), field.get(action));

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public String[] parameterNames() {
            return parameters.keySet().toArray(new String[parameters.size()]);
        }

        @Override
        public Object parameterValue(String paramName) {
            return parameters.get(paramName);
        }

        @Override
        public String[] attributeNames() {
            return attributes.keySet().toArray(new String[attributes.size()]);
        }

        @Override
        public Object get(String attrName) {
            return attributes.get(attrName);
        }

        @Override
        public void set(String attrName, Object attrValue) {
            if (attrValue == null) {
                attributes.remove(attrName);
            } else {
                attributes.put(attrName, attrValue);
            }

        }
    }
}
