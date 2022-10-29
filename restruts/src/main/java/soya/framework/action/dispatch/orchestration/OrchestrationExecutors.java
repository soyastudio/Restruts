package soya.framework.action.dispatch.orchestration;

import soya.framework.action.dispatch.ActionDispatchSession;

public class OrchestrationExecutors {
    private static PipelineExecutor pipelineExecutor = new PipelineExecutor();
    private static AggregateExecutor aggregateExecutor = new AggregateExecutor();

    public static OrchestrationExecutor pipelineExecutor() {
        return new PipelineExecutor();
    }


    public static OrchestrationExecutor aggregateExecutor() {
        return new PipelineExecutor();
    }


    private static class PipelineExecutor<T> implements OrchestrationExecutor<T> {

        @Override
        public T execute(Orchestration orchestration, ActionDispatchSession session) {
            Object result = null;
            for (String taskName : orchestration.tasks()) {
                result = orchestration.task(taskName).execute(session);

            }

            return (T) result;
        }
    }

    private static class AggregateExecutor<T> implements OrchestrationExecutor<T> {

        @Override
        public T execute(Orchestration orchestration, ActionDispatchSession session) {
            return null;
        }
    }
}
