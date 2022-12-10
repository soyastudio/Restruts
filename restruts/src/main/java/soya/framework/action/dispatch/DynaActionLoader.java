package soya.framework.action.dispatch;

import soya.framework.action.ActionName;
import soya.framework.action.servlet.ActionMappings;

import java.io.IOException;

public abstract class DynaActionLoader<T> {

    public abstract void load(T mappings) throws IOException;

    protected void addDynaDomain(DynaDomain domain) {
        DynaActionClass.add(domain);
    }

    protected void add(ActionName actionName, String dispatch) {
        new DynaActionClass(actionName, dispatch, true);
    }
}
