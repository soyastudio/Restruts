package soya.framework.action.actions.reflect;

import java.util.*;

public class ServiceInfo {
    private String name;
    private String serviceType;


    private String[] interfaces;
    private String[] superClasses;

    public ServiceInfo(String name, Object serviceInstance) {
        this.name = name;
        this.serviceType = serviceInstance.getClass().getName();

        Set<String> interfaces = new LinkedHashSet<>();
        List<String> superClasses = new ArrayList<>();
        Class<?> cls = serviceInstance.getClass();
        while(!cls.getName().equals("java.lang.Object")) {

            Arrays.asList(cls.getInterfaces()).forEach(e -> {
                interfaces.add(e.getName());
            });

            cls = cls.getSuperclass();
            superClasses.add(0, cls.getName());
        }

        this.interfaces = interfaces.toArray(new String[interfaces.size()]);
        this.superClasses = superClasses.toArray(new String[superClasses.size()]);
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public String[] getSuperClasses() {
        return superClasses;
    }
}
