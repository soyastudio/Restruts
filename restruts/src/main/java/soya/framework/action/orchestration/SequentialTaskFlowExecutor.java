package soya.framework.action.orchestration;

public class SequentialTaskFlowExecutor implements TaskFlowExecutor {

    @Override
    public void execute(TaskFlow flow, ProcessSession session) throws ProcessException {
        for (String taskName : flow.tasks()) {
            session.set(taskName, flow.task(taskName).execute(session));
        }
    }
}
