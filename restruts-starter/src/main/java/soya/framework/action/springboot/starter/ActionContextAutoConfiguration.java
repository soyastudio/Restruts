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
import soya.framework.action.ActionContextInitializer;
import soya.framework.action.ServiceLocateException;
import soya.framework.action.ServiceLocator;
import soya.framework.action.mvc.StateMachineServlet;
import soya.framework.action.servlet.ActionServlet;
import soya.framework.util.IndexedClassStore;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.StreamSupport;

@Configuration
@EnableConfigurationProperties(ActionContextProperties.class)
public class ActionContextAutoConfiguration implements ActionContextInitializer {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Autowired
    private ActionContextProperties properties;

    @PostConstruct
    void initialize() {
        Set<String> packages = new LinkedHashSet<>();
        packages.add("soya.framework");
        if (properties.getScanPackages() != null) {
            Arrays.stream(properties.getScanPackages().split(",")).forEach(e -> {
                packages.add(e.trim());
            });
        }

        new IndexedClassStore.DefaultIndexedClassStore(packages.toArray(new String[packages.size()]));
        new DefaultActionContext(this);
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

    // -----------------------
    @Override
    public ServiceLocator getServiceLocator() {
        return new ServiceLocator() {
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
        };
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> properties.setProperty(propName, environment.getProperty(propName)));
        return properties;
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationType) {
        return IndexedClassStore.getAnnotatedClasses(annotationType);
    }

    static class DefaultActionContext extends ActionContext {
        protected DefaultActionContext(ActionContextInitializer initializer) {
            super(initializer);
        }
    }

}
