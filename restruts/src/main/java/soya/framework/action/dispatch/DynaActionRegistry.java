package soya.framework.action.dispatch;

import soya.framework.action.ActionDomain;

public class DynaActionRegistry {

    public boolean addDomain(ActionDomain domain) {
        try {
            ActionDomain.builder().name(domain.getName()).create();

            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }
}
