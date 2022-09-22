package soya.framework.action.dispatch.eventbus;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.ActionDispatch;

@ActionDefinition(domain = "dispatch",
        name = "generic-pipeline-dispatch",
        path = "/dispatch/pipeline",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Generic Pipeline Dispatch",
        description = "Generic pipeline dispatch action")
public class GenericPipelineAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        PipelinePattern annotation = getClass().getAnnotation(PipelinePattern.class);
        Task[] tasks = annotation.tasks();

        for(Task task: tasks) {
            ActionDispatch actionDispatch = ActionDispatch.fromAnnotation(task.dispatch());



        }

        return null;
    }

    static class Worker {
        private String name;
        private ActionDispatch actionDispatch;


    }
}
