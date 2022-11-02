package soya.framework.action.orchestration;

import soya.framework.action.Action;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;

import java.lang.reflect.Field;

public abstract class TaskFlowAction<T> extends Action<T> {
    protected Orchestration orchestration;

    @Override
    public T execute() throws Exception {
        return (T) orchestration.execute(this);
    }

    @Override
    protected void prepare() {
        Orchestration.Builder orchestrationBuilder = Orchestration.builder();

        if(getClass().getAnnotation(ActionDefinition.class) != null) {
            Field[] fields = ActionClass.get(getClass()).getActionFields();
            for (Field field: fields) {
                orchestrationBuilder.addParameter(field.getName(), field.getType());
            }
        }

        buildTaskFlow(orchestrationBuilder.getTaskFlowBuilder());
        this.orchestration = orchestrationBuilder.create();
    }

    protected abstract void buildTaskFlow(TaskFlow.Builder taskFlowBuilder);
}
