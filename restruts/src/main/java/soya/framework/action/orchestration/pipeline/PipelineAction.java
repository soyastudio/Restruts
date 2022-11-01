package soya.framework.action.orchestration.pipeline;

import soya.framework.action.Action;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;

import java.lang.reflect.Field;

public abstract class PipelineAction<T> extends Action<T> {
    private Pipeline pipeline;

    @Override
    public T execute() throws Exception {
        return (T) pipeline.execute(this);
    }

    @Override
    protected final void prepare() throws Exception {
        Pipeline.Builder builder = Pipeline.builder();

        if(getClass().getAnnotation(ActionDefinition.class) != null) {
            Field[] fields = ActionClass.get(getClass()).getActionFields();
            for (Field field: fields) {
                builder.addParameter(field.getName(), field.getType());
            }
        }

        configure(builder);

        this.pipeline = builder.create();
    }

    protected abstract void configure(Pipeline.Builder builder);

}
