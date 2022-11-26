package soya.framework.action.orchestration.eventbus;

public interface Event {

    public String getId();

    public long getTimestamp();

    public String getAddress();

    public Object getPayload();
}
