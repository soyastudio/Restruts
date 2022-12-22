package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaClass;
import soya.framework.action.ActionCreationException;

public interface DynaActionClass extends DynaClass {
    DynaActionBean newInstance() throws ActionCreationException;
}
