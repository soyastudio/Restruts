package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionExecutor;

import java.lang.reflect.Field;

public abstract class ActionAdapterAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        ActionClass actionClass = ActionClass.get(getClass());
        ActionMapping actionMapping = actionClass.getActionType().getAnnotation(ActionMapping.class);
        Class<? extends Action> actionType = actionMapping.actionType();
        ActionExecutor executor = ActionExecutor.executor(actionType);
        for (ActionParameterSetting setting : actionMapping.parameterSettings()) {
            executor.setProperty(setting.name(), setting.value());
        }

        Field[] fields = actionClass.getActionFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getAnnotation(ActionParameter.class) != null) {
                ActionParameter ap = field.getAnnotation(ActionParameter.class);
                field.setAccessible(true);
                Object value = field.get(this);
                executor.setProperty(ap.value(), value);
            }
        }

        return convert(executor.execute());
    }

    protected T convert(Object result) {
        return (T) result;
    }

}
