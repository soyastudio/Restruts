package soya.framework.action.orchestration;

public interface TaskFlowExecutor {
    void execute(TaskFlow flow, ProcessSession session) throws ProcessException;

}
