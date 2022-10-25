package soya.framework.action.springboot.starter;

import org.springframework.beans.BeansException;
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
import soya.framework.action.ActionContext;
import soya.framework.action.ServiceLocator;
import soya.framework.action.dispatch.proxy.ActionProxyFactory;
import soya.framework.action.servlet.ActionServlet;
import soya.framework.action.servlet.StateMachineServlet;

import java.util.Arrays;
import java.util.Map;
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
        String[] packages = new String[]{"soya.framework"};
        if (environment.getProperty("soya.framework.action.scanPackages") != null) {
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
                    public String[] serviceNames() {
                        return applicationContext.getBeanDefinitionNames();
                    }

                    @Override
                    public Object getService(String name) {
                        try {
                            return applicationContext.getBean(name);

                        } catch (BeansException e) {
                            return null;
                        }
                    }

                    @Override
                    public <T> T getService(Class<T> type) {
                        try {
                            return applicationContext.getBean(type);

                        } catch (BeansException e) {
                            return null;
                        }

                    }

                    @Override
                    public <T> T getService(String name, Class<T> type) {
                        try {
                            return applicationContext.getBean(name, type);

                        } catch (BeansException e) {
                            return null;
                        }
                    }

                    @Override
                    public <T> Map<String, T> getServices(Class<T> type) {
                        try {
                            return applicationContext.getBeansOfType(type);

                        } catch (BeansException e) {
                            return null;
                        }

                    }
                })
                .setProperties(properties)
                .scan(packages)
                .create();
    }

    @Bean
    ActionProxyFactory actionProxyFactory() {
        return new ActionProxyFactory();
    }

    @Bean
    ServletRegistrationBean actionServlet() {

        ServletRegistrationBean bean = new ServletRegistrationBean(new ActionServlet(),
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
