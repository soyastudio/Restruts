package soya.framework.action;

import soya.framework.common.util.StringUtils;

import java.lang.reflect.Field;
import java.net.URI;
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

    public static ActionExecutor executor(String[] args) {
        URI uri = StringUtils.toURI(args);

        final ActionClass actionClass;
        if (uri.getScheme().equals("class")) {
            try {
                actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(uri.getHost()));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            actionClass = ActionContext.getInstance().getActionMappings().actionClass(ActionName.fromURI(uri));
        }

        if (actionClass == null) {
            throw new IllegalArgumentException("Cannot find action class from uri: " + uri);
        }

        ActionExecutor executor = new ActionExecutor(actionClass.getActionType());
        StringUtils.splitQuery(uri.getQuery()).entrySet().forEach(e -> {
            Field field = actionClass.getActionField(e.getKey());
            if (field != null) {
                executor.setProperty(field.getName(), ConvertUtils.convert(e.getValue().get(0), field.getType()));
            }

        });

        return executor;

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

        String[] cmd = new String[]{
                "class://soya.framework.action.TestAction",
                "-m",
                "XYZ"
        };

        Object result = ActionExecutor.executor(cmd).execute();
        System.out.println("---------------------- " + result);
    }
}
