package soya.framework.restruts.configuration;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.action.dispatch.EventSubscriber;
import soya.framework.action.dispatch.workflow.ActionEventBus;
import soya.framework.action.dispatch.workflow.Subscriber;

import java.util.Set;

@Configuration
public class EventBusConfiguration {

    @Bean
    ActionEventBus actionEventBus() {
        ActionEventBus bus = new ActionEventBus();
        Reflections reflections = new Reflections();
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(EventSubscriber.class);
        set.forEach(e -> {
            try {
                bus.register((Subscriber<?>) e.newInstance());

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        return bus;
    }
}
