package soya.framework.action;

public interface ActionFactory {
    ActionCallable create(ActionName actionName);
}
