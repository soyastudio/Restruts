package soya.framework.restruts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.action.dispatch.eventbus.ActionEventBus;
import soya.framework.action.dispatch.eventbus.Event;
import soya.framework.action.dispatch.eventbus.Subscriber;
import soya.framework.action.dispatch.pipeline.PipelineContainer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

@Configuration
public class ServerConfiguration {
    private static String HEARTBEAT_EVENT_ADDRESS = "timer://heartbeat";

    @Bean
    ActionEventBus actionEventBus() {
        return new DefaultActionEventBus();
    }

    @Bean
    PipelineContainer pipelineContainer(ActionEventBus eventBus) {
        File workspace = new File(System.getProperty("workspace.home"));
        File home = new File(workspace, "pipeline");
        PipelineContainer container = new PipelineContainer(home);

        eventBus.register(HEARTBEAT_EVENT_ADDRESS, new Subscriber() {
            @Override
            public void onEvent(Event event) {
                Heartbeat heartbeat = (Heartbeat) event.getSource();
                if(heartbeat.beat % 5 == 1) {
                    container.scan();
                }
            }
        });

        return container;
    }

    static class DefaultActionEventBus extends ActionEventBus {
        public DefaultActionEventBus() {
            super(Executors.newFixedThreadPool(5));
        }

        @PostConstruct
        protected void init() {
            super.init();

            new Timer().schedule(new TimerTask() {
                                     @Override
                                     public void run() {
                                         dispatch(new Event(Heartbeat.getInstance(), HEARTBEAT_EVENT_ADDRESS, null));
                                     }
                                 },
                    5000l,
                    1000);
        }
    }

    static class Heartbeat {
        private static Heartbeat me;
        private int beat;

        static {
            me = new Heartbeat();
        }

        public int getBeat() {
            return beat;
        }

        static Heartbeat getInstance() {
            me.beat++;
            return me;
        }
    }

}
