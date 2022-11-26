package soya.framework.restruts.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import soya.framework.action.dispatch.DispatchScheduler;
import soya.framework.action.orchestration.eventbus.ActionEventBus;
import soya.framework.action.orchestration.eventbus.ActionEvent;

import javax.annotation.PostConstruct;
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


    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Timer timer = new Timer("Pipeline Scanner");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //System.out.println("--------------------- !!!");
            }
        }, 1000l, 5000l);
    }

    static class DefaultActionEventBus extends ActionEventBus {
        public DefaultActionEventBus() {
            super(Executors.newFixedThreadPool(5));
        }

        @PostConstruct
        protected void init() {
            super.init();

            DispatchScheduler.getInstance();

            new Timer().schedule(new TimerTask() {
                                     @Override
                                     public void run() {
                                         dispatch(new ActionEvent(Heartbeat.getInstance(), HEARTBEAT_EVENT_ADDRESS, null));
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
