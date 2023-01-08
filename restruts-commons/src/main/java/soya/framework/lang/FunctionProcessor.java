package soya.framework.lang;

public interface FunctionProcessor<T> {
    T process(T data) throws FunctionProcessorException;
}
