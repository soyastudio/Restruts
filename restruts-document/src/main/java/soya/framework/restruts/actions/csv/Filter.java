package soya.framework.restruts.actions.csv;

import org.apache.commons.beanutils.DynaBean;

public interface Filter {
    boolean match(DynaBean bean);
}
