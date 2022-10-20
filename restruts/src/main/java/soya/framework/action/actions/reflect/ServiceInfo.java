package soya.framework.action.actions.reflect;

import java.util.ArrayList;
import java.util.List;

public class ServiceInfo {
    private String name;
    private String serviceType;
    private List<String> interfaces = new ArrayList<>();
    private List<String> superClasses = new ArrayList<>();

    public ServiceInfo(String name, String serviceType) {
        this.name = name;
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public List<String> getSuperClasses() {
        return superClasses;
    }
}
