package soya.framework.action.orchestration;

import soya.framework.action.dispatch.ActionDispatch;

import java.util.LinkedHashMap;
import java.util.Map;

public class TaskFlow<T> implements Task<T> {

    private Map<String, Task> tasks;
    private TaskFlowExecutor executor;
    private Task<T> resultHandler;

    private TaskFlow(Map<String, Task> tasks, TaskFlowExecutor executor, Task<T> resultHandler) {
        this.tasks = tasks;
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public String[] tasks() {
        return tasks.keySet().toArray(new String[tasks.size()]);
    }

    public Task<?> task(String name) {
        return tasks.get(name);
    }

    @Override
    public T execute(ProcessSession session) throws ProcessException {
        executor.execute(this, session);
        return resultHandler.execute(session);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Task> tasks = new LinkedHashMap<>();
        private TaskFlowExecutor executor;
        private Task<?> resultHandler;

        private Builder() {
        }

        public Builder addTask(String name, Task<?> task) {
            tasks.put(name, task);
            return this;
        }

        public Builder addTask(String name, String uri) {
            tasks.put(name, new ActionDispatchTask(ActionDispatch.fromURI(uri)));
            return this;
        }

        public Builder addSubFlow(String name, Builder sub) {
            tasks.put(name, sub.create());
            return this;
        }

        public Builder executor(TaskFlowExecutor executor) {
            this.executor = executor;
            return this;
        }

        public Builder resultHandler(Task<?> resultHandler) {
            this.resultHandler = resultHandler;
            return this;
        }

        public Builder resultHandler(String uri) {
            this.resultHandler = new ActionDispatchTask<>(ActionDispatch.fromURI(uri));
            return this;
        }

        public TaskFlowExecutor getExecutor() {
            return executor;
        }

        public Task<?> getResultHandler() {
            return resultHandler;
        }

        public TaskFlow create() {
            if (tasks.size() == 0) {
                throw new IllegalArgumentException("At least one task is required.");
            }

            if (executor == null) {
                throw new IllegalArgumentException("TaskFlowExecutor is required.");
            }

            return new TaskFlow(tasks, executor, resultHandler);
        }
    }

}
