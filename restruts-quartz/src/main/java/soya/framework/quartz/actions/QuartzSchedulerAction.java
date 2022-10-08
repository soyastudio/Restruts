package soya.framework.quartz.actions;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.quartz.Calendar;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "quartz-scheduler",
        name = "scheduler",
        path = "/scheduler",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Quartz Scheduler",
        description = "Quartz Scheduler")
public class QuartzSchedulerAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        Scheduler scheduler = ActionContext.getInstance().getService(Scheduler.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", scheduler.getSchedulerName());
        jsonObject.addProperty("type", scheduler.getClass().getName());

        scheduler.getCalendarNames().forEach(e -> {
            try {
                Calendar calendar = scheduler.getCalendar(e);


            } catch (SchedulerException ex) {
                ex.printStackTrace();
            }
        });

        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }
}
