package soya.framework.restruts.action;

import java.lang.reflect.Field;

public interface ActionMappings {
    String[] domains();

    Class<?> domainType(String domain);

    ActionName[] actions(String domain);

    Class<? extends ActionCallable> actionType(ActionName actionName);

    Field[] parameterFields(Class<? extends ActionCallable> actionType);
}
