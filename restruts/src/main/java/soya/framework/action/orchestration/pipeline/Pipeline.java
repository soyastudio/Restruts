package soya.framework.action.orchestration.pipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.ConvertUtils;
import soya.framework.action.dispatch.ActionDispatch;
import soya.framework.action.dispatch.ActionDispatchSession;
import soya.framework.action.dispatch.DefaultEvaluator;
import soya.framework.commons.util.ReflectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pipeline {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String name;
    private Map<String, Class<?>> parameters = new LinkedHashMap<>();
    private List<TaskNode> tasks = new ArrayList<>();

    private Pipeline() {
    }

    public String getName() {
        return name;
    }

    public Object execute(Object data) throws Exception {
        return new PipelineExecutor(this).execute(data);
    }

    public static Pipeline fromYaml(String yaml) throws IOException {
        return fromYaml(new StringReader(yaml));
    }

    public static Pipeline fromYaml(Reader reader) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(reader);
        PipelineDefinition definition = new PipelineDefinition();

        String mode = null;

        String line = bufferedReader.readLine();
        while (line != null) {
            if (line.startsWith("pipeline: ")) {
                definition.name = line.substring("pipeline: ".length()).trim();

            } else if (line.startsWith("parameters:")) {
                mode = "parameters";

            } else if (line.startsWith("tasks:")) {
                mode = "tasks";

            } else {
                String ln = line.trim();

                if (!ln.isEmpty() && ln.contains(": ")) {
                    String key = ln.substring(0, ln.indexOf(':'));
                    String value = ln.substring(ln.indexOf(": ") + 2).trim();

                    if ("parameters".equals(mode)) {
                        definition.parameters.put(key, value);

                    } else if ("tasks".equals(mode)) {
                        definition.tasks.put(key, value);
                    }
                }
            }
            line = bufferedReader.readLine();
        }

        return create(definition);

    }

    public static Pipeline fromJson(String json) {
        PipelineDefinition definition = gson.fromJson(json, PipelineDefinition.class);
        return create(definition);

    }

    public static Pipeline fromJson(Reader reader) {
        PipelineDefinition definition = gson.fromJson(JsonParser.parseReader(reader), PipelineDefinition.class);
        return create(definition);

    }

    private static Pipeline create(PipelineDefinition definition) {

        Builder builder = builder()
                .name(definition.name);

        definition.parameters().entrySet().forEach(e -> {
            try {
                builder.addParameter(e.getKey(), Class.forName(e.getValue()));
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }
        });

        definition.tasks().entrySet().forEach(e -> {
            builder.addTask(e.getKey(), ActionDispatch.fromURI(e.getValue()));
        });

        return builder.create();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Pipeline pipeline;

        private Builder() {
            pipeline = new Pipeline();
        }

        public Builder name(String name) {
            pipeline.name = name;
            return this;
        }

        public Builder addParameter(String name, Class<?> type) {
            pipeline.parameters.put(name, type);
            return this;
        }

        public Builder addTask(String name, Task task) {
            pipeline.tasks.add(new TaskNode(name, task));
            return this;
        }

        public Builder addTask(String name, String uri) {
            pipeline.tasks.add(new TaskNode(name, new ActionDispatchTask(ActionDispatch.fromURI(uri))));
            return this;
        }

        public Builder addTask(String name, ActionDispatch actionDispatch) {
            pipeline.tasks.add(new TaskNode(name, new ActionDispatchTask(actionDispatch)));
            return this;
        }

        public Pipeline create() {
            if (pipeline.tasks.size() == 0) {
                throw new IllegalStateException("At least one task is required.");
            }

            return pipeline;
        }
    }

    private static class TaskNode {
        private final String name;
        private final Task task;

        public TaskNode(String name, Task task) {
            this.name = name;
            this.task = task;
        }
    }

    private static class PipelineExecutor {
        private final Pipeline pipeline;

        PipelineExecutor(Pipeline pipeline) {
            this.pipeline = pipeline;
        }

        public Object execute(Object data) throws Exception {
            Session session = new Session();
            pipeline.parameters.entrySet().forEach(e -> {
                String name = e.getKey();
                Class<?> type = e.getValue();
                try {
                    session.inputs.put(e.getKey(), evaluate(e.getKey(), type, data));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            Queue<TaskNode> queue = new ConcurrentLinkedQueue<>();
            pipeline.tasks.forEach(e -> {
                queue.add(e);
            });

            TaskNode worker = queue.poll();
            Object result = null;
            while (worker != null) {
                result = worker.task.execute(session);
                session.results.put(worker.name, result);

                worker = queue.poll();

            }

            return result;
        }

        private Object evaluate(String name, Class<?> type, Object context) throws Exception {
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

    static class Session implements ActionDispatchSession {
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

    static class PipelineModel {
        private PipelineDefinition pipeline;
        private JsonObject data;

        public PipelineDefinition getPipeline() {
            return pipeline;
        }

        public JsonObject getData() {
            return data;
        }
    }

    static class PipelineDefinition {
        private String name;
        private Map<String, String> parameters = new LinkedHashMap<>();
        private Map<String, String> tasks = new LinkedHashMap<>();

        public String name() {
            return name;
        }

        public Map<String, String> parameters() {
            return parameters;
        }

        public Map<String, String> tasks() {
            return tasks;
        }
    }

    public static void main(String[] args) throws Exception {
        ActionContext.builder()
                .scan("soya.framework")
                .create();

        Map<String, Object> data = new HashMap<>();
        data.put("msg", "Hello World");

        Object result = Pipeline.builder()
                .name("Pipeline")
                .addParameter("msg", String.class)
                .addTask("echo", "class://soya.framework.action.actions.reflect.EchoAction?message=val(xyz%201234%20abc)")
                .addTask("encode", "text-util://base64-encode?text=ref(echo)")
                .addTask("decode", "text-util://base64-decode?text=ref(encode)")
                .create()
                .execute(data);

        System.out.println(result);

        System.exit(0);
    }

}
