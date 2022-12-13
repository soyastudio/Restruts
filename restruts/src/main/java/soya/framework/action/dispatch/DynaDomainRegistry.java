package soya.framework.action.dispatch;

public interface DynaDomainRegistry {

    DynaDomain register(String name, String path, String title, String description);

    void unregister(String name);
}
