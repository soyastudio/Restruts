package soya.framework.action.dispatch.eventbus;

import soya.framework.action.*;

import java.util.HashSet;
import java.util.Set;

public abstract class EventBusAction<T> extends Action<T> implements EventBus {

    private Set<Subscriber> subscribers = new HashSet<>();

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD,
            contentType = MediaType.APPLICATION_JSON,
            description = "Parameter values in json format.")
    protected String payload;

    public EventBusAction() {

    }

    @Override
    public T execute() throws Exception {
        return null;
    }

    public void post(Event event) {

    }
}
