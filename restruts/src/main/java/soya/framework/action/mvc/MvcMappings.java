package soya.framework.action.mvc;

import java.util.HashSet;
import java.util.Set;

public class MvcMappings {
    private Set<MvcMapping> mappings = new HashSet<>();



    static class MvcMapping {
        String path;
        String method;
        Class<? extends MvcAction> actionType;



    }
}
