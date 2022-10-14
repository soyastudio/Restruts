package soya.framework.restruts.configuration;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import soya.framework.quartz.QuartzSchedulerManager;

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
    QuartzSchedulerManager quartzSchedulerJobFactory(@Autowired Scheduler scheduler) {
        QuartzSchedulerManager factory = new QuartzSchedulerManager(scheduler);
        factory.load();

        return factory;
    }

}
