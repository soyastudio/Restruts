package soya.framework.action;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public abstract class Action<T> implements ActionCallable {

    @Override
    public ActionResult call() {
        logger().fine("start executing...");

        try {
            checkRequiredProperties();
            prepare();
            T t = execute();
            ActionResult result = getActionClass().createResult(this, t);

            logger().fine("executed successfully.");
            return result;

        } catch (Exception e) {
            logger().severe(new StringBuilder()
                    .append(e.getClass().getName())
                    .append("[")
                    .append(e.getMessage())
                    .append("]")
                    .toString());

            return getActionClass().createResult(this, e);
        }
    }

    public abstract T execute() throws Exception;

    protected ActionClass getActionClass() {
        return ActionClass.get(getClass());
    }

    protected void prepare() throws Exception {
    }

    protected void checkRequiredServices() {
    }

    protected void checkRequiredProperties() {
        Field[] fields = ActionClass.get(getClass()).getActionFields();
        for (Field field : fields) {
            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
            if (actionProperty.required()) {
                field.setAccessible(true);
                try {
                    if (field.get(this) == null) {
                        throw new IllegalStateException("Required field '"
                                + field.getName() + "' is not set for action '"
                                + getClass().getName() + "'.");
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

    protected Object getService(String name) throws ServiceNotAvailableException {
        return ActionContext.getInstance().getService(name);
    }

    protected <T> T getService(Class<T> type) throws ServiceNotAvailableException {
        return ActionContext.getInstance().getService(type);
    }

    protected <T> T getService(String name, Class<T> type) throws ServiceNotAvailableException {
        return ActionContext.getInstance().getService(name, type);
    }
}
