package soya.framework.quartz;

import org.quartz.*;
import org.reflections.Reflections;

import java.util.Set;

public class QuartzSchedulerJobFactory {
    private Scheduler scheduler;

    public QuartzSchedulerJobFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void load(String... packaged) {
        Reflections reflections = new Reflections();
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(ScheduledDispatchPattern.class);
        set.forEach(e -> {
            ScheduledDispatchPattern annotation = e.getAnnotation(ScheduledDispatchPattern.class);
            Key jobKey = new Key(annotation.jobId());
            Key triggerKey = new Key(annotation.triggerId());

            JobDetail job = JobBuilder.newJob((Class<? extends Job>) e)
                    .withIdentity(jobKey.name, jobKey.group)
                    .build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey.name, triggerKey.group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(annotation.cronExpression()))
                    .build();

            try {
                scheduler.scheduleJob(job, cronTrigger);

            } catch (SchedulerException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }

        });
    }

    private static class Key {
        private String group;
        private String name;

        private Key(String id) {
            if (id.contains("://")) {
                int index = id.indexOf("://");
                group = id.substring(0, index);
                name = id.substring(index + 3);
            } else {
                name = id;
            }
        }
    }

}
