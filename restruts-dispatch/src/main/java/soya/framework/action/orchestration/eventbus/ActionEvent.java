package soya.framework.action.orchestration.eventbus;

import soya.framework.action.ActionName;

import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class ActionEvent extends EventObject implements Event {

    private final String id;
    private final long timestamp;

    private final String address;
    private final Object payload;

    private ActionEvent parent;
    private List<ActionEvent> children = new ArrayList<>();

    public ActionEvent(Object source, String address, Object payload) {
        super(source);
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();

        this.address = address;
        this.payload = payload;

        if (source instanceof ActionEvent) {
            ActionEvent event = (ActionEvent) source;
            this.parent = event;
            parent.children.add(this);

        }

        ActionName actionName = ActionName.fromURI(URI.create(address));

    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAddress() {
        return address;
    }

    public Object getPayload() {
        return payload;
    }

    public ActionEvent getParent() {
        return parent;
    }

    public List<ActionEvent> getChildren() {
        return children;
    }
}
