package soya.framework.action.patterns;

import soya.framework.action.*;

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

        Pipeline pipeline = actionClass.getActionType().getAnnotation(Pipeline.class);
        Task[] tasks = pipeline.tasks();
        for (Task task : tasks) {
            ActionSignature signature = ActionSignature.fromURI(task.signature());
            queue.add(new Worker(task.name(), signature));
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
        private final ActionSignature signature;

        private Worker(String name, ActionSignature signature) {
            this.name = name;
            this.signature = signature;
        }

        void execute(Session session) {
            session.currentTask = name;
            ActionResult result = create(signature, session).call();

            session.results.put(name, result);
            session.out = result;
        }

        private ActionCallable create(ActionSignature signature, Session session) {

            return null;
        }

    }
}
