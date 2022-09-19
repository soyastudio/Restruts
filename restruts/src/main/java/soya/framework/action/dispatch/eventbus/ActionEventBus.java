package soya.framework.action.dispatch.eventbus;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionName;
import soya.framework.action.dispatch.EventSubscriber;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class ActionEventBus implements EventBus {

    private Set<Registration> registry = new HashSet<>();

    @Override
    public void register(Subscriber<?> subscriber) {
        ActionEventSubscriber sub = (ActionEventSubscriber) subscriber;
        EventSubscriber annotation = sub.getClass().getAnnotation(EventSubscriber.class);
        if(annotation != null) {
            registry.add(new Registration(ActionName.fromURI(URI.create(annotation.subscribe())), sub));
        }
    }

    public void post(Event<?> event) {
        registry.forEach(e -> {
            if (event.address().equals(e.actionName.toString())) {
                ActionContext.getInstance().getExecutorService().execute(() -> e.subscriber.onEvent(event));
            }
        });
    }

    private static class Registration {
        private final ActionName actionName;
        private final ActionEventSubscriber subscriber;

        private Registration(ActionName actionName, ActionEventSubscriber subscriber) {
            this.actionName = actionName;
            this.subscriber = subscriber;
        }
    }
}
