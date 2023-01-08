package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaClass;
import soya.framework.action.ActionCreationException;
import soya.framework.action.ActionDescription;

public interface DynaActionClass extends DynaClass {
    ActionDescription getActionDescription();

    DynaActionBean newInstance() throws ActionCreationException;
}
