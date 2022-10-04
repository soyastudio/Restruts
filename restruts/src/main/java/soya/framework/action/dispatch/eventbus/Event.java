package soya.framework.action.dispatch.eventbus;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class Event extends EventObject {

    private final String id;
    private final long timestamp;

    private final String address;
    private final Object payload;

    private Event parent;
    private List<Event> children = new ArrayList<>();

    public Event(Object source, String address, Object payload) {
        super(source);
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();

        this.address = address;
        this.payload = payload;

        if(source instanceof Event) {
            Event event = (Event) source;
            this.parent = event;
            parent.children.add(this);

        }
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

    public Event getParent() {
        return parent;
    }

    public List<Event> getChildren() {
        return children;
    }
}
