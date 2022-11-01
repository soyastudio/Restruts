package soya.framework.action.orchestration;

public class Pipeline {

    private Orchestration orchestration;

    private Pipeline(Orchestration orchestration) {
        this.orchestration = orchestration;
    }

    public Object execute(Object input) throws ProcessException {
        return orchestration.execute(input);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Orchestration.Builder orchestrationBuilder;
        private String result;

        private Builder() {
            this.orchestrationBuilder = Orchestration.builder();
        }

        public Builder name(String name) {
            orchestrationBuilder.name(name);
            return this;
        }

        public Builder addParameter(String name, Class<?> type) {
            orchestrationBuilder.addParameter(name, type);
            this.result = name;
            return this;
        }

        public Builder addTask(String name, Task<?> task) {
            orchestrationBuilder.addTask(name, task);
            this.result = name;
            return this;
        }

        public Builder addTask(String name, String uri) {
            orchestrationBuilder.addTask(name, uri);
            this.result = name;
            return this;
        }

        public Builder addSubFlow(String name, TaskFlow.Builder sub) {
            orchestrationBuilder.addSubFlow(name, sub);
            this.result = name;
            return this;
        }

        public Orchestration create() {
            return orchestrationBuilder
                    .executor(new SequentialTaskFlowExecutor())
                    .resultHandler(session -> session.get(result))
                    .create();
        }
    }
}
