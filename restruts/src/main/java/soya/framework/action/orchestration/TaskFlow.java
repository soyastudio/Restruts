package soya.framework.action.orchestration;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.Assignment;
import soya.framework.action.dispatch.DefaultEvaluator;
import soya.framework.action.dispatch.Evaluator;

import java.util.LinkedHashMap;
import java.util.Map;

public class TaskFlow<T> implements Task<T> {

    private Map<String, Task> tasks = new LinkedHashMap<>();
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

    private static class ActionDispatchTask<T> implements Task<T> {
        private ActionDispatch actionDispatch;

        private ActionDispatchTask(ActionDispatch actionDispatch) {
            this.actionDispatch = actionDispatch;
        }

        @Override
        public T execute(ProcessSession session) {
            ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(actionDispatch.getActionName());
            ActionCallable action = actionDispatch.create(session, new DefaultEvaluator(new ParameterEvaluator(), new ReferenceEvaluator()));

            return (T) action.call().get();
        }
    }

    private static class ParameterEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            ProcessSession session = (ProcessSession) context;
            return session.parameterValue(assignment.getExpression());
        }
    }

    private static class ReferenceEvaluator implements Evaluator {

        @Override
        public Object evaluate(Assignment assignment, Object context, Class<?> type) {
            ProcessSession session = (ProcessSession) context;
            return session.get(assignment.getExpression());
        }
    }

}
