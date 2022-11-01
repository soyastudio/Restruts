package soya.framework.action.orchestration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ParallelTaskFlowExecutor extends ConcurrentTaskFlowExecutor {
    public ParallelTaskFlowExecutor() {
        super();
    }

    public ParallelTaskFlowExecutor(ExecutorService executorService, long timeout) {
        super(executorService, timeout);
    }

    @Override
    public void execute(TaskFlow flow, ProcessSession session) throws ProcessException {
        long startTime = System.currentTimeMillis();

        Map<String, Future> futures = new HashMap<>();
        for (String taskName : flow.tasks()) {
            Task<?> task = flow.task(taskName);
            Future<?> future = executorService().submit((Callable<?>) () -> task.execute(session));
            futures.put(taskName, future);

            try {
                Thread.sleep(50l);
            } catch (InterruptedException e) {
                throw new ProcessException(e);
            }
        }

        boolean processing = true;
        while (processing) {
            if(System.currentTimeMillis() - startTime > timeout()) {
                throw new ProcessException("Process time out.");
            }

            processing = false;
            for (Future e : futures.values()) {
                if (!e.isDone()) {
                    processing = true;
                    break;
                }
            }

            try {
                Thread.sleep(50l);
            } catch (InterruptedException e) {
                throw new ProcessException(e);
            }
        }

        futures.entrySet().forEach(e -> {
            try {
                session.set(e.getKey(), e.getValue().get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
