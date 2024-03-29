package soya.framework.restruts.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.dispatch.DispatchScheduler;
import soya.framework.action.dispatch.DynaActionDispatchActionRegistry;
import soya.framework.action.dispatch.proxy.ActionProxyFactory;
import soya.framework.action.dispatch.proxy.ActionProxyPattern;
import soya.framework.action.orchestration.eventbus.ActionEvent;
import soya.framework.action.orchestration.eventbus.ActionEventBus;
import soya.framework.util.ClassIndexUtils;
import soya.framework.util.LogUtils;
import soya.framework.util.logging.Sl4jDelegateService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

@Configuration
public class ServerConfiguration {
    private static String HEARTBEAT_EVENT_ADDRESS = "timer://heartbeat";

    @PostConstruct
    void init() {

        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(ActionDefinition.class));

        String basePackage = "soya/framework";
        Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
        for (BeanDefinition component : components) {
            System.out.println("=============== " + component.getBeanClassName());


            //System.out.printf("Component: %s\n", component.getBeanClassName());
        }

        long start = LogUtils.logStartNanoTime();

        //new JulLoggingService();
        new Sl4jDelegateService();

        LogUtils.logEndNanoTime(start, "method execute");
    }

    @Bean
    DynaActionDispatchActionRegistry dynaActionRegistry() {
        DynaActionDispatchActionRegistry registry = new DynaActionDispatchActionRegistry(DynaActionDispatchActionRegistry.NAME);

        LogUtils.info("XYZ", "TODO: load DynaDispatchAction Classes!");

        ActionContext.getInstance().getActionRegistrationService().register(registry);
        return registry;
    }

    @Bean
    ActionEventBus actionEventBus() {
        return new DefaultActionEventBus();
    }

    @Bean
    ActionProxyFactory actionProxyFactory() {
        ActionProxyFactory proxyFactory = new ActionProxyFactory();
        ClassIndexUtils.getAnnotatedClasses(ActionProxyPattern.class).forEach(e -> {
            if (e.isInterface()) {
                proxyFactory.create(e);
            }
        });

        return proxyFactory;
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Timer timer = new Timer("Pipeline Scanner");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.log(ServerConfiguration.class, "============ pipeline scanner");
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
