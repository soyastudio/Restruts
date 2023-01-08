package soya.framework.action.orchestration.pipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import soya.framework.action.ActionName;
import soya.framework.action.orchestration.Pipeline;
import soya.framework.action.orchestration.TaskFlow;

import java.io.Serializable;

public class PipelineParser {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Pipeline fromYaml(String yaml) {
        return null;
    }

    public static Pipeline fromJson(String json) {
        PipelineModel model = GSON.fromJson(json, PipelineModel.class);
        Pipeline.Builder builder = Pipeline.builder();

        builder.name(ActionName.create(model.domain, model.name).toString());

        if (model.parameterTypes != null) {
            model.parameterTypes.entrySet().forEach(e -> {
                try {
                    builder.addParameter(e.getKey(), Class.forName(e.getValue().getAsString()));
                } catch (ClassNotFoundException ex) {
                    throw new IllegalArgumentException(ex);
                }
            });
        }

        if (model.tasks == null || model.tasks.length == 0) {
            throw new IllegalArgumentException("At lease one task is required.");
        }

        for (TaskModel taskModel : model.tasks) {
            if (taskModel.dispatch != null) {
                builder.addTask(taskModel.name, taskModel.dispatch);

            } else if (taskModel.subFlow != null) {
                TaskFlow.Builder subFlowBuilder = TaskFlow.builder();
                buildSubFlow(taskModel.subFlow, subFlowBuilder);
                builder.addSubFlow(taskModel.name, subFlowBuilder);
            }
        }

        return builder.create();

    }

    private static void buildSubFlow(SubFlowModel model, TaskFlow.Builder builder) {
        for (TaskModel taskModel : model.tasks) {
            if (taskModel.dispatch != null) {
                builder.addTask(taskModel.name, taskModel.dispatch);

            } else if (taskModel.subFlow != null) {
                TaskFlow.Builder subFlowBuilder = TaskFlow.builder();
                buildSubFlow(taskModel.subFlow, subFlowBuilder);
                builder.addSubFlow(taskModel.name, subFlowBuilder);
            }
        }

        if(model.dispatch != null) {
            builder.resultHandler(model.dispatch);

        } else if(model.subFlow != null) {
            TaskFlow.Builder subFlowBuilder = TaskFlow.builder();
            buildSubFlow(model.subFlow, subFlowBuilder);
            builder.resultHandler(subFlowBuilder.create());
        }
    }


    static class PipelineModel implements Serializable {
        private String domain = "pipeline";
        private String name;

        private JsonObject parameterTypes;
        private TaskModel[] tasks;
    }

    static class TaskModel {
        private String name;
        private String dispatch;
        private SubFlowModel subFlow;

    }

    static class SubFlowModel {
        private TaskModel[] tasks;

        private String processor;

        private String dispatch;
        private SubFlowModel subFlow;
    }

}
