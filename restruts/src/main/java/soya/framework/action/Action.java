package soya.framework.action;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Action<T> implements ActionCallable {

    @Override
    public ActionResult call() {
        logger().fine("start executing...");

        try {
            checkRequiredProperties();
            T t = execute();
            ActionResult result = new DefaultActionResult(this, t);
            logger().fine("executed successfully");
            return result;

        } catch (Exception e) {
            logger().severe(e.getMessage());
            return new DefaultActionResult(this, e);
        }
    }

    public abstract T execute() throws Exception;

    protected void checkRequiredProperties() throws Exception {
        Field[] fields = ActionClass.get(getClass()).getActionFields();
        for (Field field : fields) {
            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
            if (actionProperty.required()) {
                field.setAccessible(true);
                try {
                    if (field.get(this) == null) {
                        throw new IllegalStateException("Required field '"
                                + field.getName() + "' is not set for action: "
                                + getClass().getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    protected ActionContext context() {
        return ActionContext.getInstance();
    }

    protected Logger logger() {
        return Logger.getLogger(getClass().getName());
    }

    private static final class DefaultActionResult implements ActionResult {

        private final String uri;
        private final long timestamp;
        private final Object value;

        private DefaultActionResult(ActionCallable action, Object value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();

            ActionClass actionClass = ActionClass.get(action.getClass());
            StringBuilder builder = new StringBuilder(actionClass.getActionName().toString());
            Field[] fields = actionClass.getActionFields();
            Map<String, Object> values = new LinkedHashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(action);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (fieldValue != null) {
                    values.put(field.getName(), fieldValue);
                }
            }

            if (values.size() > 0) {
                builder.append("?");
                values.entrySet().forEach(e -> {
                    builder.append(e.getKey())
                            .append("=")
                            .append(e.getValue())
                            .append("&");
                });
                builder.deleteCharAt(builder.length() - 1);
            }

            this.uri = builder.toString();

        }

        public static ActionResult create(ActionCallable action, Object value) {
            return new DefaultActionResult(action, value);
        }

        public String uri() {
            return uri;
        }

        public Object get() {
            return value;
        }

        public boolean success() {
            return !(value instanceof Throwable);
        }

        public boolean empty() {
            return value == null;
        }
    }
}
