package soya.framework.action.dispatch.pipeline;

import soya.framework.action.dispatch.ActionDispatch;

public abstract class AnnotatedPipelineAction<T> extends PipelineAction<T> {

    protected void configure(Pipeline.Builder builder) {
        PipelinePattern pattern = getClass().getAnnotation(PipelinePattern.class);
        if (pattern != null) {
            TaskDefinition[] tasks = pattern.tasks();
            for (TaskDefinition task : tasks) {
                builder.addTask(task.name(), ActionDispatch.fromURI(task.dispatch()));
            }
        }
    }
}
