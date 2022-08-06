package soya.framework.reststruts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import soya.framework.struts.action.ActionServlet;

@SpringBootApplication
public class RestStrutsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestStrutsApplication.class, args);
    }

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
            bean.addInitParameter(ActionServlet.INIT_PARAM_SCAN_PACKAGES, RestStrutsApplication.class.getPackage().getName());

        }

        return bean;
    }
}
