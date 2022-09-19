package soya.framework.action.dispatch;

import soya.framework.action.ActionName;

import java.util.EventObject;

public class Event extends EventObject {

    private ActionName address;

    public Event(ActionName address, Object source) {
        super(source);
        this.address = address;
    }
}
