package soya.framework.action.dispatch.eventbus;

import soya.framework.action.ActionName;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class ActionEvent<T> extends EventObject implements Event<T> {

    private ActionName actionName;
    private String id;
    private long timestamp;
    private T data;

    private ActionEvent parent;
    private List<ActionEvent> children = new ArrayList<>();

    public ActionEvent(Object source, ActionName actionName, T data) {
        super(source);
        this.actionName = actionName;
        this.data = data;

        this.timestamp = System.currentTimeMillis();
        this.id = UUID.randomUUID().toString();

        if(source instanceof ActionEvent) {
            this.parent = ((ActionEvent) source).parent;
            this.parent.children.add(this);
        }
    }

    @Override
    public String address() {
        return actionName.toString();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public T getData() {
        return data;
    }

}
