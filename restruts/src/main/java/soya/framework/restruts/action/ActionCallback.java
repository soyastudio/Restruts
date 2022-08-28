package soya.framework.restruts.action;

public interface ActionCallback {
    void onComplete(Object result);

    void onException(Exception e);
}
