package soya.framework.restruts.action;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Action<T> implements ActionCallable {

    @Override
    public ActionResult call() {
        try {
            T t = execute();
            return new DefaultActionResult(this, t);

        } catch (Exception e) {
            return new DefaultActionResult(this, e);
        }
    }

    public abstract T execute() throws Exception;

    private static final class DefaultActionResult implements ActionResult {

        private final ActionCallable action;
        private final Object value;
        private final long timestamp;
        private ActionName actionName;

        private DefaultActionResult(ActionCallable action, Object value) {
            this.action = action;
            this.value = value;
            this.timestamp = System.currentTimeMillis();

            OperationMapping operationMapping = action.getClass().getAnnotation(OperationMapping.class);
            if (operationMapping != null) {
                actionName = ActionName.create(operationMapping.domain(), operationMapping.name());
            } else {
                actionName = ActionName.create("class", action.getClass().getName());
            }

        }

        public static ActionResult create(ActionCallable action, Object value) {
            return new DefaultActionResult(action, value);
        }

        public ActionName name() {
            return actionName;
        }

        public String uri() {
            StringBuilder builder = new StringBuilder(actionName.toString());
            Field[] fields = ActionContext.getInstance().getActionMappings().parameterFields((Class<? extends Action>) action.getClass());

            Map<String, Object> values = new LinkedHashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(action);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (value != null) {
                    values.put(field.getName(), value);
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

            return builder.toString();
        }

        public ActionCallable action() {
            return action;
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
