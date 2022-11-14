package soya.framework.action;

public interface ActionMappings {

    String[] domains();

    Class<?> domainType(String domain);

    ActionName[] actions(String domain);

    ActionClass actionClass(ActionName actionName);

    ActionClass actionClass(Class<? extends ActionCallable> actionType);

}
