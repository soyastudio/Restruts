package soya.framework.bean;

public interface DynaBeanExecutor<T> {
    T execute(DynaBean<?> bean);
}
