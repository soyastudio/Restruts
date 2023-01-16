package soya.framework.pattern;

public interface FunctionalFilter<T> {
    T process(T data) throws FunctionalFilterException;
}
