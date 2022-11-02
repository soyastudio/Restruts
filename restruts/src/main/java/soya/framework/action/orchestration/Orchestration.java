package soya.framework.action.orchestration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.beanutils.PropertyUtils;
import soya.framework.action.ActionCallable;
import soya.framework.action.ActionContext;
import soya.framework.action.ConvertUtils;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Orchestration {

    private String name;
    private Map<String, Class<?>> parameterTypes;
    private TaskFlow taskFlow;

    private Orchestration(String name, Map<String, Class<?>> parameterTypes, TaskFlow taskFlow) {
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.taskFlow = taskFlow;
    }

    public String getName() {
        return name;
    }

    public Object execute(Object input) throws ProcessException {
        Session session = new Session(name);
        parameterTypes.entrySet().forEach(e -> {
            session.parameters.put(e.getKey(), evaluate(e.getKey(), e.getValue(), input));
        });

        return taskFlow.execute(session);
    }

    private Object evaluate(String name, Class<?> type, Object context) {
        if (context instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) context;
            return ConvertUtils.convert(map.get(name), type);

        } else if (context instanceof JsonObject) {
            Gson gson = new Gson();
            JsonObject jsonObject = (JsonObject) context;
            return gson.fromJson(jsonObject.get(name), type);

        } else if (context instanceof ActionCallable) {
            try {
                Field field = ReflectUtils.findField(context.getClass(), name);
                field.setAccessible(true);

                return ConvertUtils.convert(field.get(context), type);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return ConvertUtils.convert(PropertyUtils.getProperty(context, name), type);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Map<String, Class<?>> parameterTypes = new LinkedHashMap<>();
        private TaskFlow.Builder taskFlowBuilder = TaskFlow.builder();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addParameter(String name, Class<?> type) {
            parameterTypes.put(name, type);
            return this;
        }

        public Builder addTask(String name, Task<?> task) {
            taskFlowBuilder.addTask(name, task);
            return this;
        }

        public Builder addTask(String name, String uri) {
            taskFlowBuilder.addTask(name, uri);
            return this;
        }

        public Builder addSubFlow(String name, TaskFlow.Builder sub) {
            taskFlowBuilder.addSubFlow(name, sub);
            return this;
        }

        public Builder executor(TaskFlowExecutor executor) {
            taskFlowBuilder.executor(executor);
            return this;
        }

        public Builder resultHandler(Task<?> resultHandler) {
            taskFlowBuilder.resultHandler(resultHandler);
            return this;
        }

        public Builder resultHandler(String uri) {
            taskFlowBuilder.resultHandler(uri);
            return this;
        }

        public TaskFlow.Builder getTaskFlowBuilder() {
            return taskFlowBuilder;
        }

        public TaskFlowExecutor getExecutor() {
            return taskFlowBuilder.getExecutor();
        }

        public Task<?> getResultHandler() {
            return taskFlowBuilder.getResultHandler();
        }

        public Orchestration create() {
            return new Orchestration(name, parameterTypes, taskFlowBuilder.create());
        }
    }

    static class Session implements ProcessSession {
        private final String name;
        private final String id;
        private final long createdTime;

        private Map<String, Object> parameters = new LinkedHashMap<>();
        private Map<String, Object> attributes = new HashMap<>();

        Session(String name) {
            this.name = name;
            this.id = UUID.randomUUID().toString();
            this.createdTime = System.currentTimeMillis();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public String[] parameterNames() {
            return parameters.keySet().toArray(new String[parameters.size()]);
        }

        @Override
        public Object parameterValue(String paramName) {
            return parameters.get(paramName);
        }

        @Override
        public String[] attributeNames() {
            return attributes.keySet().toArray(new String[attributes.size()]);
        }

        @Override
        public Object get(String attrName) {
            return attributes.get(attrName);
        }

        @Override
        public void set(String attrName, Object attrValue) {
            if (attrValue == null) {
                attributes.remove(attrName);
            } else {
                attributes.put(attrName, attrValue);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        ActionContext.builder()
                .scan("soya.framework")
                .create();

        Map<String, Object> data = new HashMap<>();
        data.put("msg", "Hello World");

        Object result = Aggregator.builder()
                .name("Aggregator")
                .addParameter("msg", String.class)
                .addTask("echo", "class://soya.framework.action.actions.reflect.EchoAction?message=val(Hello)")
                .addTask("encode", "text-util://base64-encode?text=param(msg)")
                .resultHandler("text-util://base64-decode?text=ref(encode)")
                .create()
                .execute(data);

        System.out.println(result);

        System.exit(0);
    }
}
