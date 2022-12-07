package soya.framework.action.dispatch;

import soya.framework.action.ActionName;

import java.util.HashSet;
import java.util.Set;

public abstract class DynaActionLoader {

    protected Set<DynaDomain> domains = new HashSet<>();
    protected Set<DynaActionClass> classes = new HashSet<>();



    protected void addDynaDomain(DynaDomain domain) {
        DynaActionClass.add(domain);
    }

    protected void add(ActionName actionName, String dispatch) {
        new DynaActionClass(actionName, dispatch, true);
    }
}
