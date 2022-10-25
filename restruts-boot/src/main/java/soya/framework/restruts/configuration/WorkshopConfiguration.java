package soya.framework.restruts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.action.dispatch.proxy.ActionProxyBuilder;
import soya.framework.restruts.pattern.Workshop;

@Configuration
public class WorkshopConfiguration {

    @Bean
    Workshop workshop() {
        return new ActionProxyBuilder<Workshop>(Workshop.class).create();
    }

}
