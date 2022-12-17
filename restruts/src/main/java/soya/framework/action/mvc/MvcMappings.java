package soya.framework.action.mvc;

import java.util.*;

public final class MvcMappings {
    public static final String ATTRIBUTE_NAME = "soya.framework.action.MvcMappings";

    private Map<String, Class<? extends MvcAction>> actions = new HashMap<>();

    private Set<MvcMapping> mappings = new HashSet<>();

    public void add(Class<? extends MvcAction> actionType) {
        if (actionType.getAnnotation(MvcDefinition.class) != null) {
            MvcDefinition definition = actionType.getAnnotation(MvcDefinition.class);
        }
    }

    public MvcMapping get(String method, String path) {
        Iterator<MvcMapping> iterator = mappings.iterator();
        while (iterator.hasNext()) {
            MvcMapping mapping = iterator.next();
            if (mapping.getMethod().equals(method) && mapping.getPath().equals(path)) {
                return mapping;
            }
        }

        return null;
    }

}
