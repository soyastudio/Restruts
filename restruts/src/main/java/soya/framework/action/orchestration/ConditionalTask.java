package soya.framework.action.orchestration;

public class ConditionalTask<T> implements Task<T> {

    private String condition;

    private String dispatch;

    @Override
    public T execute(ProcessSession session) throws ProcessException {

        return null;
    }
}
