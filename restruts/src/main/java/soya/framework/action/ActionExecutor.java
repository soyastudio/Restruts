package soya.framework.action;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

public final class ActionExecutor {

    private Class<? extends ActionCallable> actionType;
    private Map<String, Field> fieldMap = new LinkedHashMap<>();
    private Map<String, Boolean> requiredSettings = new LinkedHashMap<>();

    private ActionCallable action;

    private ActionExecutor(Class<? extends ActionCallable> actionType) {
        this.actionType = actionType;

        ActionClass actionClass = ActionClass.get(actionType);
        Field[] fields = actionClass.getActionFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }

        try {
            action = actionType.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);

        }
    }

    public static ActionExecutor executor(Class<? extends ActionCallable> actionType) {
        return new ActionExecutor(actionType);
    }

    public ActionExecutor setProperty(String name, Object value) {
        if (!fieldMap.containsKey(name)) {
            throw new IllegalArgumentException("Field does not exist: " + name);
        }

        if (value != null) {
            Field field = fieldMap.get(name);
            field.setAccessible(true);
            try {
                field.set(action, ConvertUtils.convert(value, field.getType()));
                if (requiredSettings.containsKey(field.getName())) {
                    requiredSettings.put(field.getName(), Boolean.TRUE);
                }

            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return this;
    }

    public Object execute() throws Exception {
        fieldMap.values().forEach(e -> {
            ActionProperty property = e.getAnnotation(ActionProperty.class);
            e.setAccessible(true);
            try {
                if (e.get(action) == null && !property.defaultValue().isEmpty()) {
                    e.set(action, ConvertUtils.convert(property.defaultValue(), e.getType()));
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        checkRequired();
        ActionResult result = action.call();
        if (result.success()) {
            return result.get();

        } else {
            throw (Exception) result.get();
        }
    }

    public Future<ActionResult> submit() {
        checkRequired();
        return ActionContext.getInstance().getExecutorService().submit(action);
    }

    public void call(ActionCallback callback) {
        checkRequired();
        ActionContext.getInstance().getExecutorService()
                .execute(() -> callback.onActionResult(action.call()));

    }

    private void checkRequired() {
        requiredSettings.entrySet().forEach(e -> {
            if (!e.getValue()) {
                throw new IllegalStateException("Required property is not set: " + e.getKey());
            }
        });
    }

    public static void main(String[] args) throws Exception {

        String signature = "class://soya.framework.action.TestAction?message=ref(msg)";
        Map<String, Object> params = new HashMap<>();
        params.put("msg", "Hello World!");

        if (ActionContext.getInstance() == null) {
            ActionContext.builder().create();
        }

        ActionExecutor.executor(TestAction.class)
                .setProperty("message", "Good morning!")
                .call(result -> {
                    if (result.success()) {
                        //System.out.println("----------------------" + result.get());

                    } else {

                    }

                    System.exit(0);
                });
    }
}
