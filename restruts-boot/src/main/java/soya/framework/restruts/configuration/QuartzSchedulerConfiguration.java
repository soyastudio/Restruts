package soya.framework.restruts.configuration;

import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import soya.framework.quartz.QuartzSchedulerJobFactory;

import java.util.Properties;

@Configuration
public class QuartzSchedulerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzProperties quartzProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        //factory.setDataSource(dataSource);
        factory.setQuartzProperties(properties);
        return factory;
    }

    @Bean
    QuartzSchedulerJobFactory quartzSchedulerJobFactory(@Autowired Scheduler scheduler) {
        QuartzSchedulerJobFactory factory = new QuartzSchedulerJobFactory(scheduler);
        factory.load();

        return factory;
    }

}
