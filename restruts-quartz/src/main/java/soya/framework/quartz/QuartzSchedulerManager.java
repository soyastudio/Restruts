package soya.framework.quartz;

import org.quartz.*;
import org.quartz.Calendar;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.reflections.Reflections;

import java.util.*;

public class QuartzSchedulerManager {

    private Scheduler scheduler;

    public QuartzSchedulerManager(Scheduler scheduler) {
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

    public SchedulerMetaData schedulerMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }





    public void standby() {
        try {
            scheduler.standby();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

    }

    public List<TriggerKey> scheduledJobs() throws SchedulerException {
        return new ArrayList<>(scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));

    }

    public Date createScheduledJob(String jobId, String triggerId, String cronExpression, String actionDispatch) throws SchedulerException {

        Key jobKey = new Key(jobId);
        Key triggerKey = new Key(triggerId);

        JobDetail job = JobBuilder.newJob(ActionDispatchJob.class)
                .withIdentity(jobKey.name, jobKey.group)
                .build();
        job.getJobDataMap().put(ActionDispatchJob.ACTION_DISPATCH, actionDispatch);

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey.name, triggerKey.group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        return scheduler.scheduleJob(job, cronTrigger);
    }

    public String getDispatchJobDetails(String jobId) throws SchedulerException {
        Key key = new Key(jobId);
        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(key.name, key.group));

        return jobDetail.getJobDataMap().getString(ActionDispatchJob.ACTION_DISPATCH);
    }

    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }

    public void setJobFactory(JobFactory jobFactory) throws SchedulerException {
        scheduler.setJobFactory(jobFactory);
    }

    public ListenerManager getListenerManager() throws SchedulerException {
        return scheduler.getListenerManager();
    }

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(trigger);
    }

    public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> map, boolean b) throws SchedulerException {
        scheduler.scheduleJobs(map, b);
    }

    public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> set, boolean b) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, set, b);
    }

    public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.unscheduleJob(triggerKey);
    }

    public boolean unscheduleJobs(List<TriggerKey> list) throws SchedulerException {
        return scheduler.unscheduleJobs(list);
    }

    public Date rescheduleJob(TriggerKey triggerKey, Trigger trigger) throws SchedulerException {
        return scheduler.rescheduleJob(triggerKey, trigger);
    }

    public void addDispatchJob(JobDetail jobDetail, boolean b) throws SchedulerException {
        scheduler.addJob(jobDetail, b);
    }

    public void addJob(JobDetail jobDetail, boolean b, boolean b1) throws SchedulerException {
        scheduler.addJob(jobDetail, b, b1);
    }

    public boolean deleteJob(JobKey jobKey) throws SchedulerException {
        return scheduler.deleteJob(jobKey);
    }

    public boolean deleteJobs(List<JobKey> list) throws SchedulerException {
        return scheduler.deleteJobs(list);
    }

    public void triggerJob(JobKey jobKey) throws SchedulerException {
        scheduler.triggerJob(jobKey);
    }

    public void triggerJob(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException {
        scheduler.triggerJob(jobKey, jobDataMap);
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    public void pauseJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        scheduler.pauseJobs(groupMatcher);
    }

    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey);
    }

    public void pauseTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        scheduler.pauseTriggers(groupMatcher);
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    public void resumeJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        scheduler.resumeJobs(groupMatcher);
    }

    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey);
    }

    public void resumeTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        scheduler.resumeTriggers(groupMatcher);
    }

    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }

    public List<String> getJobGroupNames() throws SchedulerException {
        return scheduler.getJobGroupNames();
    }

    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        return scheduler.getJobKeys(groupMatcher);
    }

    public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    public List<String> getTriggerGroupNames() throws SchedulerException {
        return scheduler.getTriggerGroupNames();
    }

    public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        return scheduler.getTriggerKeys(groupMatcher);
    }

    public Set<String> getPausedTriggerGroups() throws SchedulerException {
        return scheduler.getPausedTriggerGroups();
    }

    public JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
        return scheduler.getJobDetail(jobKey);
    }

    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTrigger(triggerKey);
    }

    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTriggerState(triggerKey);
    }

    public void resetTriggerFromErrorState(TriggerKey triggerKey) throws SchedulerException {
        scheduler.resetTriggerFromErrorState(triggerKey);
    }

    public void addCalendar(String s, Calendar calendar, boolean b, boolean b1) throws SchedulerException {
        scheduler.addCalendar(s, calendar, b, b1);
    }

    public boolean deleteCalendar(String s) throws SchedulerException {
        return scheduler.deleteCalendar(s);
    }

    public Calendar getCalendar(String s) throws SchedulerException {
        return scheduler.getCalendar(s);
    }

    public List<String> getCalendarNames() throws SchedulerException {
        return scheduler.getCalendarNames();
    }

    public boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException {
        return scheduler.interrupt(jobKey);
    }

    public boolean interrupt(String s) throws UnableToInterruptJobException {
        return scheduler.interrupt(s);
    }

    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    public boolean checkExists(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.checkExists(triggerKey);
    }

    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    public static class Key {
        private final String name;
        private String group;

        private Key(String id) {
            if (id.contains("://")) {
                int index = id.indexOf("://");
                group = id.substring(0, index);
                name = id.substring(index + 3);
            } else {
                name = id;
            }
        }

        public String getName() {
            return name;
        }

        public String getGroup() {
            return group;
        }
    }

    public static class ScheduledJob {
        private String jobId;
        private String triggerId;
        private String cronExpression;
        private String actionDispatch;
    }

}
