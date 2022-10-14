package soya.framework.quartz.actions;

import org.quartz.TriggerKey;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "quartz-scheduler",
        name = "list-trigger-keys",
        path = "/trigger-keys",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON
)
public class ListTriggerKeysAction extends QuartzSchedulerAction<TriggerKey[]>{
    @Override
    public TriggerKey[] execute() throws Exception {
        List<TriggerKey> triggerKeyList = quartzSchedulerManager().scheduledJobs();
        Collections.sort(triggerKeyList);
        return triggerKeyList.toArray(new TriggerKey[triggerKeyList.size()]);
    }
}
