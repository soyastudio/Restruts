package soya.framework.action.orchestration;

import java.util.concurrent.ExecutorService;

public final class Aggregator {

    private Orchestration orchestration;

    private Aggregator(Orchestration orchestration) {
        this.orchestration = orchestration;
    }

    public Object execute(Object data) throws ProcessException {
        return orchestration.execute(data);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ExecutorService executorService, long timeout) {
        return new Builder();
    }

    public static class Builder {
        private Orchestration.Builder orchestrationBuilder;

        private Builder() {
            orchestrationBuilder = Orchestration.builder()
                    .executor(new ParallelTaskFlowExecutor());
        }

        private Builder(ExecutorService executorService, long timeout) {
            orchestrationBuilder = Orchestration.builder()
                    .executor(new ParallelTaskFlowExecutor(executorService, timeout));
        }

        public Builder name(String name) {
            orchestrationBuilder.name(name);
            return this;
        }

        public Builder addParameter(String name, Class<?> type) {
            orchestrationBuilder.addParameter(name, type);
            return this;
        }

        public Builder addTask(String name, Task<?> task) {
            orchestrationBuilder.addTask(name, task);
            return this;
        }

        public Builder addTask(String name, String uri) {
            orchestrationBuilder.addTask(name, uri);
            return this;
        }

        public Builder addSubFlow(String name, TaskFlow.Builder sub) {
            orchestrationBuilder.addSubFlow(name, sub);
            return this;
        }

        public Builder resultHandler(Task<?> resultHandler) {
            orchestrationBuilder.resultHandler(resultHandler);
            return this;
        }

        public Builder resultHandler(String uri) {
            orchestrationBuilder.resultHandler(uri);
            return this;
        }

        public Orchestration create() {
            if (orchestrationBuilder.getResultHandler() == null) {
                throw new IllegalArgumentException("Result Handler is required.");
            }

            return orchestrationBuilder.create();
        }
    }


}
