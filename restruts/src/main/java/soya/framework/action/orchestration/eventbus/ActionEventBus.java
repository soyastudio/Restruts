package soya.framework.action.orchestration.eventbus;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class ActionEventBus implements EventBus {
    private static Logger logger = Logger.getLogger(ActionEventBus.class.getName());

    private ExecutorService executorService;

    private Set<Registration> subscribers = new HashSet<>();

    private Queue<Event> queue;

    protected ActionEventBus(ExecutorService executorService) {
        this.executorService = executorService;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void register(String address, Subscriber subscriber) {
        subscribers.add(new Registration(address, subscriber));
    }

    @Override
    public void dispatch(Event event) {
        //logger.fine("Dispatching event: " + event.getAddress());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Registration)) return false;
            Registration that = (Registration) o;
            return Objects.equals(address, that.address) && Objects.equals(subscriber, that.subscriber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address, subscriber);
        }
    }
}
