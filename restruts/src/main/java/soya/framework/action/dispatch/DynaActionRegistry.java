package soya.framework.action.dispatch;

public interface DynaActionRegistry {

    void register(String name, String path, String title, String description);
}
