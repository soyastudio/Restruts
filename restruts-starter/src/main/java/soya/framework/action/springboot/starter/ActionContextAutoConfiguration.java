package soya.framework.action.springboot.starter;

import org.reflections.Reflections;
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
import soya.framework.action.*;
import soya.framework.action.dispatch.proxy.ActionProxyFactory;
import soya.framework.action.dispatch.proxy.ActionProxyPattern;
import soya.framework.action.mvc.StateMachineServlet;
import soya.framework.action.servlet.*;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
                    public Object getService(String name) throws ServiceLocateException {
                        try {
                            return applicationContext.getBean(name);

                        } catch (BeansException e) {
                            throw new ServiceLocateException("Service not available for name: " + name + ".", e);
                        }
                    }

                    @Override
                    public <T> T getService(Class<T> type) {
                        try {
                            return applicationContext.getBean(type);

                        } catch (BeansException e) {
                            throw new ServiceLocateException("Service not available for type: " + type.getName() + ".", e);
                        }

                    }

                    @Override
                    public <T> T getService(String name, Class<T> type) {
                        try {
                            return applicationContext.getBean(name, type);

                        } catch (BeansException e) {
                            throw new ServiceLocateException("Service not available for type: " + type.getName() + " with name: " + name + ".", e);
                        }
                    }

                    @Override
                    public <T> Map<String, T> getServices(Class<T> type) {
                        try {
                            return applicationContext.getBeansOfType(type);

                        } catch (BeansException e) {
                            throw new ServiceLocateException("Service not available for type: " + type.getName() + ".", e);
                        }

                    }
                })
                .setProperties(properties)
                .scan(packages)
                .create();
    }

    @Bean
    ActionMappings actionMapping(ActionContext actionContext, ServletContext servletContext) {
        ActionMappings mappings = new ActionMappings();
        for (ActionDomain domain : ActionDomain.domains()) {
            mappings.addDomain(domain.getName(), domain.getPath(), domain.getTitle(), domain.getDescription());
        }

        for (ActionName actionName : ActionClass.actions()) {
            if (!mappings.containsDomain(actionName.getDomain())) {
                mappings.addDomain(actionName.getDomain());
            }

            ActionClass actionClass = ActionClass.get(actionName);
            ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);
            ActionMapping mapping = mappings.add(actionName, definition.method().name(), definition.path(), definition.produces()[0]);

            mapping.addDescriptions(definition.description());
            mapping.addDescriptions("- Action name: " + actionName);
            mapping.addDescriptions("- Action class: " + actionClass.getActionType().getName());

            for (Field field : actionClass.getActionFields()) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                ParameterMapping pm = new ParameterMapping(field.getName(), actionProperty.parameterType());
                pm.addDescriptions(actionProperty.description());

                mapping.getParameters().add(pm);
            }

        }

        servletContext.setAttribute(ActionMappings.ACTION_MAPPINGS_ATTRIBUTE, mappings);

        return mappings;

    }

    @Bean
    ActionProxyFactory actionProxyFactory() {
        ActionProxyFactory proxyFactory = new ActionProxyFactory();

        Reflections reflections = new Reflections();
        Set<Class<?>> proxyInterfaces = reflections.getTypesAnnotatedWith(ActionProxyPattern.class);
        proxyInterfaces.forEach(e -> {
            if (e.isInterface()) {
                proxyFactory.create(e);
            }
        });

        return proxyFactory;
    }

    @Bean
    ServletRegistrationBean actionServlet() {

        ServletRegistrationBean bean = new ServletRegistrationBean(new ActionServlet(),
                "/api/*");
        bean.setLoadOnStartup(5);

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
