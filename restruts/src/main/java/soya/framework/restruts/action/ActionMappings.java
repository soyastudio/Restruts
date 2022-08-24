package soya.framework.restruts.action;

import java.lang.reflect.Field;

public interface ActionMappings {
    String[] domains();

    Class<?> domainType(String domain);

    ActionName[] actions(String domain);

    Class<? extends Action> actionType(ActionName actionName);

    Field[] parameterFields(Class<? extends Action> actionType);
}
