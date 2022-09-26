package soya.framework.action.dispatch.workflow;

import soya.framework.action.ActionName;
import soya.framework.action.ActionResult;
import soya.framework.action.dispatch.ActionDispatch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Pipeline {
    private final ActionName actionName;
    private Map<String, Class<?>> parameters = new LinkedHashMap<>();
    private List<TaskNode> tasks = new ArrayList<>();

    private Pipeline(ActionName actionName) {
        this.actionName = actionName;
    }

    public static Builder builder(ActionName actionName) {
        return new Builder(actionName);
    }

    public static class Builder {
        private Pipeline pipeline;

        private Builder(ActionName actionName) {
            this.pipeline = new Pipeline(actionName);
        }

        public Builder addParameter(String name, Class<?> type) {
            pipeline.parameters.put(name, type);
            return this;
        }

        public Builder addTask(String name, String uri) {
            pipeline.tasks.add(new TaskNode(name, ActionDispatch.fromURI(uri)));
            return this;
        }

        public Pipeline create() {
            return pipeline;
        }
    }

    static class TaskNode {
        private final String name;
        private final ActionDispatch actionDispatch;

        public TaskNode(String name, ActionDispatch actionDispatch) {
            this.name = name;
            this.actionDispatch = actionDispatch;
        }
    }

    public static class PipelineExecutor {
        private final Pipeline pipeline;
        private Map<String, Object> data;

        public PipelineExecutor(Pipeline pipeline) {
            this.pipeline = pipeline;
        }

        public ActionResult execute() {
            return null;
        }
    }

}
