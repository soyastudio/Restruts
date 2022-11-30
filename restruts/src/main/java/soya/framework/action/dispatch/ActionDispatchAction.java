package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.lang.reflect.Field;

public abstract class ActionDispatchAction extends Action<Object> {

    @Override
    public Object execute() throws Exception {

        ActionDispatchPattern actionDispatchPattern = getClass().getAnnotation(ActionDispatchPattern.class);
        return ActionDispatch.fromURI(actionDispatchPattern.uri()).dispatch(this, new AssignmentEvaluator() {
            @Override
            protected Object fromParameter(String exp, Object context, Class<?> type) {
                try {
                    Field field = context.getClass().getDeclaredField(exp);
                    field.setAccessible(true);
                    return field.get(context);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected Object fromReference(String exp, Object context, Class<?> type) {
                throw new RuntimeException("");
            }
        }).get();
    }

}
