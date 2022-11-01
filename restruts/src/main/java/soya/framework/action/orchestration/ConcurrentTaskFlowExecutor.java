package soya.framework.action.orchestration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ConcurrentTaskFlowExecutor implements TaskFlowExecutor {

    public static long DEFAULT_TIMEOUT = 10000l;

    private final ExecutorService executorService;
    private final long timeout;

    public ConcurrentTaskFlowExecutor() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.timeout = DEFAULT_TIMEOUT;
    }

    public ConcurrentTaskFlowExecutor(ExecutorService executorService, long timeout) {
        this.executorService = executorService;
        this.timeout = Math.max(DEFAULT_TIMEOUT, timeout);
    }

    protected ExecutorService executorService() {
        return executorService;
    }

    protected long timeout() {
        return timeout;
    }
}
