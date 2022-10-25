package soya.framework.action.dispatch.pipeline;

import soya.framework.action.*;
import soya.framework.action.dispatch.ActionDispatch;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class PipelineAction<T> extends Action<T> {

    @Override
    public T execute() throws Exception {
        Queue<Worker> queue = new ConcurrentLinkedQueue<>();
        Map<String, Object> inputs = new LinkedHashMap<>();

        ActionClass actionClass = ActionClass.get(getClass());
        Field[] fields = actionClass.getActionFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(this);
            inputs.put(field.getName(), value);
        }

        Session session = new Session(inputs);

        PipelinePattern pipelinePattern = actionClass.getActionType().getAnnotation(PipelinePattern.class);
        Task[] tasks = pipelinePattern.tasks();
        for (Task task : tasks) {
            ActionDispatch signature = ActionDispatch.fromAnnotation(task.dispatch());
        }

        while (!queue.isEmpty()) {
            Worker worker = queue.poll();
            worker.execute(session);
        }

        throw new RuntimeException("TEST ERROR");
    }

    protected T getResult(Session session) {
        return (T) session.out.get();
    }

    public static final class Session {

        private long timestamp;
        private Map<String, Object> inputs;
        private Map<String, ActionResult> results;

        private String currentTask;
        private ActionResult out;

        public Session(final Map<String, Object> inputs) {
            this.timestamp = System.currentTimeMillis();
            this.inputs = Collections.unmodifiableMap(inputs);
            this.results = new LinkedHashMap<>();
        }

    }

    private static class Worker {
        private final String name;
        private final ActionDispatch signature;

        private Worker(String name, ActionDispatch signature) {
            this.name = name;
            this.signature = signature;
        }

        void execute(Session session) {
            session.currentTask = name;
            ActionResult result = create(signature, session).call();

            session.results.put(name, result);
            session.out = result;
        }

        private ActionCallable create(ActionDispatch signature, Session session) {

            return null;
        }

    }
}
