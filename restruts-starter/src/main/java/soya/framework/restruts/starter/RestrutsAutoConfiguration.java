package soya.framework.restruts.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import soya.framework.restruts.action.ActionServlet;
import soya.framework.restruts.action.StateMachineServlet;

@Configuration
@EnableConfigurationProperties(RestrutsProperties.class)
public class RestrutsAutoConfiguration {

    @Autowired
    private RestrutsProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Bean
    ServletRegistrationBean actionServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new ActionServlet(),
                "/api/*");
        bean.setLoadOnStartup(10);

        if (environment.getProperty(ActionServlet.INIT_PARAM_SCAN_PACKAGES) != null) {
            bean.addInitParameter(ActionServlet.INIT_PARAM_SCAN_PACKAGES, environment.getProperty(ActionServlet.INIT_PARAM_SCAN_PACKAGES));

        } else {
            String[] bootNames = applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class);
            if (bootNames != null && bootNames.length == 1) {
                bean.addInitParameter(ActionServlet.INIT_PARAM_SCAN_PACKAGES, applicationContext.getBean(bootNames[0]).getClass().getPackage().getName());
            }

        }

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
