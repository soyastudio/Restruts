package soya.framework.action.dispatch;

import soya.framework.action.ActionResult;

public interface FragmentProcessor<T> {
    T process(ActionResult in);
}
