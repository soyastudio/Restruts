package soya.framework.cache.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "cache",
        name = "cache-names",
        path = "/manager/cache-names",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON
)
public class CacheNamesAction extends CacheAction<String[]>{

    @Override
    public String[] execute() throws Exception {
        List<String> list = new ArrayList<>();
        cacheManager.getCacheNames().forEach(e -> {
            list.add(e);
        });
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }
}
