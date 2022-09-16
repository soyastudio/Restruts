package soya.framework.action.dispatch;

import soya.framework.action.*;
import soya.framework.common.util.ReflectUtils;

import java.lang.reflect.Field;
import java.net.URI;

public abstract class ActionDispatchAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        ActionMapping actionMapping = getClass().getAnnotation(ActionMapping.class);
        ActionName actionName = ActionName.fromURI(URI.create(actionMapping.uri()));
        ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(actionName);
        Class<? extends ActionCallable> actionType = actionClass.getActionType();
        ActionCallable action = actionClass.newInstance();

        for(ActionParameter parameter: actionMapping.parameters()) {
            AssignmentMethod assignmentMethod = parameter.assignmentMethod();
            Field field = ReflectUtils.findField(actionType, parameter.name());
            Object value = null;
            if(AssignmentMethod.VALUE.equals(assignmentMethod)) {
                value = parameter.expression();

            } else if (AssignmentMethod.PARAMETER.equals(assignmentMethod)) {
                Field paramField = ReflectUtils.findField(getClass(), parameter.expression());
                paramField.setAccessible(true);
                value = paramField.get(this);

            }

            if(value != null) {
                field.setAccessible(true);
                field.set(action, ConvertUtils.convert(value, field.getType()));
            }
        }

        ActionResult result = action.call();
        if(result.success()) {
            return convert(result.get());

        } else  {
            throw (Exception) result.get();
        }
    }

    protected T convert(Object result) {
        return (T) result;
    }

}
