package soya.framework.action.dispatch;

public class DispatchClassTemplate {
    private String packageName = "{{packageName}}";
    private String className = "{{className}}";

    private String domain = "{{domain}}";
    private String name = "{{name}}";
    private String path = "{{path}}";
    private String httpMethod = "{{GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS}}";
    private String produce = "{{TEXT_PLAIN|APPLICATION_JSON|APPLICATION_XML|APPLICATION_OCTET_STREAM|...}}";


}
