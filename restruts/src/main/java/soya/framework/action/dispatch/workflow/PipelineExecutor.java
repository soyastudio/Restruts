package soya.framework.action.dispatch.workflow;

import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.ActionDispatch;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PipelineExecutor {

    private Queue<Worker> queue = new ConcurrentLinkedQueue<>();

    private PipelineExecutor() {
    }

    public PipelineExecutor addTask(String name, ActionDispatch action) {
        queue.add(new Worker(name, action));
        return this;
    }

    public ActionResult execute() {
        while (!queue.isEmpty()) {
            Worker worker = queue.poll();
            //worker.execute(session);
        }

        return null;
    }

    public static PipelineExecutor fromAnnotation(PipelinePattern annotation) {
        PipelineExecutor executor = newInstance();
        for (Task task : annotation.tasks()) {
            executor.addTask(task.name(), ActionDispatch.fromAnnotation(task.dispatch()));
        }

        return executor;
    }

    public static PipelineExecutor newInstance() {
        return new PipelineExecutor();
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
        private final ActionDispatch actionDispatch;

        private Worker(String name, ActionDispatch actionDispatch) {
            this.name = name;
            this.actionDispatch = actionDispatch;
        }

        void execute(Session session) {

        }

    }
}
