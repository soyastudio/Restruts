package soya.framework.action;

import java.util.Collection;

public interface ActionRegistry {
    Collection<ActionName> actions();

    ActionFactory actionFactory();

}
