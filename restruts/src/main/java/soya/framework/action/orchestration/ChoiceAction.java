package soya.framework.action.orchestration;

public abstract class ChoiceAction<T> extends CompositeTaskAction<Choice<T>, T> {

    @Override
    protected Choice<T> build() {
        Choice.Builder builder = Choice.builder();

        return builder.create();
    }

    protected abstract void buildChoice(Choice.Builder builder);
}
