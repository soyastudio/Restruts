package soya.framework.action;

public final class ActionClasses {
    private ActionClasses() {
    }

    public static ActionName[] getActionNames() {
        return ActionClass.actionNames();
    }

    public static long getTotalExecutedActionCount() {
        return ActionClass.totalCount();
    }

    public static long getExecutedActionCount(ActionName actionName) {
        return ActionClass.actionCount(actionName);
    }
}
