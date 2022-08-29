package soya.framework.restruts.action;

import java.io.Serializable;

public interface ActionResult extends Serializable {

    ActionName name();

    String uri();

    ActionCallable action();

    Object get();

    boolean success();

    boolean empty();
}
