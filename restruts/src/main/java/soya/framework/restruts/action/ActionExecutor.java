package soya.framework.restruts.action;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Future;

public final class ActionExecutor {

    private Class<? extends ActionCallable> actionType;
    private Map<String, Field> fieldMap = new LinkedHashMap<>();
    private Map<String, Boolean> requiredSettings = new LinkedHashMap<>();
    private ActionCallable action;

    private ActionExecutor(Class<? extends ActionCallable> actionType) {
        this.actionType = actionType;

        List<Field> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        Class<?> cls = actionType;
        while (!cls.getName().equals("java.lang.Object")) {
            for (Field field : cls.getDeclaredFields()) {
                if (field.getAnnotation(ParameterMapping.class) != null
                        || field.getAnnotation(PayloadMapping.class) != null
                        || !fieldNames.contains(field.getName())) {

                    fields.add(field);
                    fieldNames.add(field.getName());

                    boolean required = false;
                    if (field.getAnnotation(ParameterMapping.class) != null) {
                        required = field.getAnnotation(ParameterMapping.class).required();

                    } else if (field.getAnnotation(PayloadMapping.class) != null) {
                        required = field.getAnnotation(PayloadMapping.class).required();

                    }

                    if (required) {
                        requiredSettings.put(field.getName(), Boolean.FALSE);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        Collections.sort(fields, new ActionMappings.ParameterFieldComparator());

        fields.forEach(e -> {
            fieldMap.put(e.getName(), e);
        });

        try {
            action = actionType.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);

        }
    }

    public static ActionExecutor executor(Class<? extends Action> actionType) {
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
        if (ActionContext.getInstance() == null) {
            ActionContext.builder().create();
        }

        ActionExecutor.executor(TestAction.class)
                .setProperty("message", "Good morning!")
                .call(result -> {
                    if(result.success()) {
                        System.out.println("----------------------" + result.get());

                    } else {

                    }

                    System.exit(0);
                });
    }
}
