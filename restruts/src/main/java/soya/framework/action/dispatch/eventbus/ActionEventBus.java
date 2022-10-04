package soya.framework.action.dispatch.eventbus;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ActionEventBus implements EventBus {
    private ExecutorService executorService;

    private Set<Registration> subscribers = new HashSet<>();

    protected ActionEventBus(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void register(String address, Subscriber subscriber) {
        subscribers.add(new Registration(address, subscriber));
    }

    @Override
    public void dispatch(Event event) {
        synchronized (subscribers) {
            subscribers.forEach(e -> {
                if (event.getAddress().equals(e.address)) {
                    executorService.execute(() -> e.subscriber.onEvent(event));
                }
            });
        }
    }

    protected void init() {
        Reflections reflections = new Reflections();
        Set<Class<?>> subs = reflections.getTypesAnnotatedWith(EventSubscriber.class);
        subs.forEach(e -> {
            EventSubscriber eventSubscriber = e.getAnnotation(EventSubscriber.class);
            try {
                register(eventSubscriber.address(), (ActionDispatchSubscriber) e.newInstance());

            } catch (InstantiationException | IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        });
    }

    static class Registration {
        private final String address;
        private final Subscriber subscriber;

        public Registration(String address, Subscriber subscriber) {
            this.address = address;
            this.subscriber = subscriber;
        }
    }
}
