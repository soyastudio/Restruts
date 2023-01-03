package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaBean;
import soya.framework.action.ActionCallable;

public interface DynaActionBean<T extends DynaActionClass> extends DynaBean, ActionCallable {

    @Override
    T getDynaClass();
}
