package soya.framework.restruts.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import soya.framework.restruts.action.ActionContext;
import soya.framework.restruts.action.ServiceLocator;
import soya.framework.restruts.action.servlet.SpringActionServlet;
import soya.framework.restruts.action.servlet.StateMachineServlet;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

@Configuration
@EnableConfigurationProperties(ActionContextProperties.class)
public class ActionContextAutoConfiguration {

    @Autowired
    private ActionContextProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Bean
    ActionContext actionContext() {
        String[] packages = new String[] {"soya.framework"};
        if(environment.getProperty("soya.framework.action.scanPackages") != null) {
            packages = environment.getProperty("soya.framework.action.scanPackages").split(",");
        }

        Properties properties = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> properties.setProperty(propName, environment.getProperty(propName)));


        return ActionContext.builder()
                .serviceLocator(new ServiceLocator() {
                    @Override
                    public <T> T getService(Class<T> type) {
                        return applicationContext.getBean(type);
                    }

                    @Override
                    public <T> T getService(String name, Class<T> type) {
                        return applicationContext.getBean(name, type);
                    }
                })
                .setProperties(properties)
                .scan(packages)
                .create();
    }

    @Bean
    ServletRegistrationBean actionServlet() {

        ServletRegistrationBean bean = new ServletRegistrationBean(new SpringActionServlet(),
                "/api/*");
        bean.setLoadOnStartup(10);

        return bean;
    }

    @Bean
    ServletRegistrationBean stateMachineServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StateMachineServlet(),
                "*.do");
        bean.setLoadOnStartup(10);

        return bean;
    }

}
