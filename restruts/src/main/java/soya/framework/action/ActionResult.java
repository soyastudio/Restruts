package soya.framework.action;

import java.io.Serializable;

public interface ActionResult extends Serializable {

    String uri();

    Object get();

    boolean success();

    boolean empty();
}
