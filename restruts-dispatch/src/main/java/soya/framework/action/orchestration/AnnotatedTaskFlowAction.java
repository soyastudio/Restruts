package soya.framework.action.orchestration;

import soya.framework.action.ActionContext;

public class AnnotatedTaskFlowAction<T> extends TaskFlowAction<T> {

    @Override
    protected void buildTaskFlow(TaskFlow.Builder taskFlowBuilder) {
        TaskFlowDefinition taskFlowDefinition = getClass().getAnnotation(TaskFlowDefinition.class);
        if(taskFlowDefinition == null) {
            throw new IllegalArgumentException("This class is not annotated as 'TaskFlowDefinition': " + getClass().getName());
        }

        for(TaskDefinition taskDefinition: taskFlowDefinition.tasks()) {
            taskFlowBuilder.addTask(taskDefinition.name(), taskDefinition.dispatch());
        }

        taskFlowBuilder
                .executor(findTaskFlowExecutor(taskFlowDefinition.processor()))
                .resultHandler(taskFlowDefinition.resultHandler());
    }

    private TaskFlowExecutor findTaskFlowExecutor(String executor) {
        if(ActionContext.getInstance().getService(executor) != null) {
            return ActionContext.getInstance().getService(executor, TaskFlowExecutor.class);

        }

        try {
            Class<? extends TaskFlowAction> cls = (Class<? extends TaskFlowAction>) Class.forName(executor);
            if(ActionContext.getInstance().getService(cls) != null) {
                return (TaskFlowExecutor) ActionContext.getInstance().getService(cls);

            } else {
                return (TaskFlowExecutor) cls.newInstance();

            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
