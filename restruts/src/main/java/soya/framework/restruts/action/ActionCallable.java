package soya.framework.restruts.action;

import java.util.concurrent.Callable;

public interface ActionCallable extends Callable<ActionResult> {

    @Override
    ActionResult call();
}
