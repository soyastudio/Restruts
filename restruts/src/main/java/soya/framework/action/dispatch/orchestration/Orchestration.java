package soya.framework.action.dispatch.orchestration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.ConvertUtils;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.ActionDispatchSession;
import soya.framework.action.dispatch.DefaultEvaluator;
import soya.framework.action.dispatch.pipeline.Task;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class Orchestration<T> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String name;
    private Map<String, Class<?>> parameters = null;
    protected Map<String, TaskNode> tasks = null;

    private final OrchestrationExecutor<T> executor;
    private Task resultHandler;

    private Orchestration(OrchestrationExecutor<T> executor) {
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public String[] tasks() {
        return tasks.keySet().toArray(new String[tasks.size()]);
    }

    public Task<?> task(String name) {
        return tasks.get(name).task;
    }

    public Object execute(Object input) {
        Session session = new Session();
        parameters.entrySet().forEach(e -> {
            String name = e.getKey();
            Class<?> type = e.getValue();
            try {
                session.inputs.put(e.getKey(), evaluate(e.getKey(), type, input));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        executor.execute(this, session);

        if(resultHandler != null) {
            return resultHandler.execute(session);
        } else {
            return null;
        }
    }

    public OrchestrationBuilder builder() {
        return new OrchestrationBuilder();
    }

    private static Object evaluate(String name, Class<?> type, Object context) throws Exception {
        if (context instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) context;
            return ConvertUtils.convert(map.get(name), type);

        } else if (context instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) context;
            return gson.fromJson(jsonObject.get(name), type);

        } else if (context instanceof ActionCallable) {
            Field field = ReflectUtils.findField(context.getClass(), name);
            field.setAccessible(true);
            Object value = field.get(context);

            return ConvertUtils.convert(value, type);

        } else {
            Object value = PropertyUtils.getProperty(context, name);
            return ConvertUtils.convert(value, type);
        }
    }

    private static class TaskNode {
        private String name;
        private Task<?> task;

        private TaskNode(String name, Task<?> task) {
            this.name = name;
            this.task = task;
        }
    }

    private static class ActionDispatchTask<T> implements Task<T> {
        private ActionDispatch actionDispatch;

        private ActionDispatchTask(ActionDispatch actionDispatch) {
            this.actionDispatch = actionDispatch;
        }

        @Override
        public T execute(ActionDispatchSession session) {
            ActionClass actionClass = ActionContext.getInstance().getActionMappings().actionClass(actionDispatch.getActionName());
            ActionCallable action = actionDispatch.create(session, new DefaultEvaluator());

            return (T) action.call().get();
        }
    }

    private static class Session implements ActionDispatchSession {
        private Map<String, Object> inputs = new LinkedHashMap<>();
        private Map<String, Object> results = new LinkedHashMap<>();

        @Override
        public String[] parameterNames() {
            return inputs.keySet().toArray(new String[inputs.size()]);
        }

        @Override
        public Object parameterValue(String paramName) {
            return inputs.get(paramName);
        }

        @Override
        public Map<String, Object> data() {
            return results;
        }
    }

    public final static class OrchestrationBuilder {

        private String name;
        private Map<String, Class<?>> parameters = new LinkedHashMap<>();
        private Map<String, TaskNode> tasks = new LinkedHashMap();

        private Task resultHandler;

        private OrchestrationBuilder() {
        }

        public OrchestrationBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrchestrationBuilder addParameter(String name, Class<?> type) {
            parameters.put(name, type);
            return this;
        }

        public OrchestrationBuilder addTask(String name, Task task) {
            tasks.put(name, new TaskNode(name, task));
            return this;
        }

        public OrchestrationBuilder addTask(String name, String uri) {
            tasks.put(name, new TaskNode(name, new ActionDispatchTask(ActionDispatch.fromURI(uri))));
            return this;
        }

        public OrchestrationBuilder addTask(String name, ActionDispatch actionDispatch) {
            tasks.put(name, new TaskNode(name, new ActionDispatchTask(actionDispatch)));
            return this;
        }

        public OrchestrationBuilder resultHandler(Task<?> resultHandler) {
            this.resultHandler = resultHandler;
            return this;
        }

        public <T> Orchestration<T> create(OrchestrationExecutor<T> executor) {
            if (executor == null) {
                throw new IllegalArgumentException("OrchestrationExecutor is required.");
            }

            if (tasks.size() == 0) {
                throw new IllegalStateException("At least one task is required.");
            }

            Orchestration<T> orchestration = new Orchestration<>(executor);
            orchestration.name = name;
            orchestration.parameters = parameters;
            orchestration.tasks = tasks;
            orchestration.resultHandler = resultHandler;

            return orchestration;
        }

        public <T> Orchestration<T> sequentialOrchestration() {
            return create(new SequentialExecutor<>());
        }

        public <T> Orchestration<T> parallelOrchestration() {
            return create(new ParallelExecutor<>());
        }

    }

    private static class SequentialExecutor<T> implements OrchestrationExecutor<T> {

        @Override
        public T execute(Orchestration orchestration, ActionDispatchSession session) {
            Object result = null;
            for (String taskName : orchestration.tasks()) {
                result = orchestration.task(taskName).execute(session);

            }

            return (T) result;
        }
    }

    private static class ParallelExecutor<T> implements OrchestrationExecutor<T> {

        @Override
        public T execute(Orchestration orchestration, ActionDispatchSession session) {


            return null;
        }
    }
}
