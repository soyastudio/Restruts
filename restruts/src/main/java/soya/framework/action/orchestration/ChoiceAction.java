package soya.framework.action.orchestration;

public class ChoiceAction<T> extends CompositeTaskAction<T> {
    private Choice choice;

    @Override
    public T execute() throws Exception {
        return (T) choice.execute(newSession());
    }
}
