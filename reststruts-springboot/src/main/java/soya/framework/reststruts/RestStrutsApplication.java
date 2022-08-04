package soya.framework.reststruts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import soya.framework.action.servlet.ActionServlet;

@SpringBootApplication
public class RestStrutsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestStrutsApplication.class, args);
    }

    @Bean
    ServletRegistrationBean actionServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new ActionServlet(),
                "/api/*");
        bean.setLoadOnStartup(10);

        return bean;
    }
}
