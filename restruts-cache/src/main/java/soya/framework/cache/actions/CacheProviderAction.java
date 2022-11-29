package soya.framework.cache.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import javax.cache.spi.CachingProvider;

@ActionDefinition(
        domain = "cache",
        name = "caching-provider",
        path = "/manager/caching-provider",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON
)
public class CacheProviderAction extends CacheAction<String> {

    @Override
    public String execute() throws Exception {
        CachingProvider provider = cacheManager.getCachingProvider();

        System.out.println("============== " + provider.getDefaultURI());

        return "cacheManager";
    }
}
