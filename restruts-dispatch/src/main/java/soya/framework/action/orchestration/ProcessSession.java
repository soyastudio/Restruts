package soya.framework.action.orchestration;

public interface ProcessSession {

    String getName();

    long getCreatedTime();

    String getId();

    String[] parameterNames();

    Object parameterValue(String paramName);

    String[] attributeNames();

    Object get(String attrName);

    void set(String attrName, Object attrValue);

}
