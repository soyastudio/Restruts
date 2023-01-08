package soya.framework.action.orchestration.choice;

import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.orchestration.ActionDispatchTask;
import soya.framework.action.orchestration.ProcessException;
import soya.framework.action.orchestration.ProcessSession;
import soya.framework.action.orchestration.Task;

import java.util.LinkedHashMap;
import java.util.Map;

public class Choice<T> implements Task<T> {

    private Map<Condition, Task> tasks;
    private Task otherwise;

    private Choice(Map<Condition, Task> tasks, Task otherwise) {
        this.tasks = tasks;
        this.otherwise = otherwise;
    }

    @Override
    public T execute(ProcessSession session) throws ProcessException {
        Task task = otherwise;
        for (Map.Entry<Condition, Task> entry : tasks.entrySet()) {

            if (entry.getKey().execute(session)) {
                return (T) entry.getValue().execute(session);
            }
        }

        return (T) task.execute(session);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<Condition, Task> tasks = new LinkedHashMap<>();
        private Task otherwise;

        private Builder() {
        }

        public Builder when(Condition condition, String dispatch) {
            tasks.put(condition, new ActionDispatchTask(ActionDispatch.fromURI(dispatch)));
            return this;
        }

        public Builder when(Condition condition, Task<?> task) {
            tasks.put(condition, task);
            return this;
        }

        public Builder otherwise(String dispatch) {
            this.otherwise = new ActionDispatchTask(ActionDispatch.fromURI(dispatch));
            return this;
        }

        public Builder otherwise(Task task) {
            return this;
        }

        public Choice create() {
            return new Choice(tasks, otherwise);
        }
    }
}
