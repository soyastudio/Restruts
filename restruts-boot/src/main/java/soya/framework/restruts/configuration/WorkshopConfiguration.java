package soya.framework.restruts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.action.dispatch.ActionProxyBuilder;
import soya.framework.restruts.service.Workshop;

@Configuration
public class WorkshopConfiguration {

    @Bean
    Workshop workshop() {
        return new ActionProxyBuilder<Workshop>(Workshop.class).create();
    }

}
