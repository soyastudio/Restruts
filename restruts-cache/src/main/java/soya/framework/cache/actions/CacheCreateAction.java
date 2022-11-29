package soya.framework.cache.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

@ActionDefinition(
        domain = "cache",
        name = "cache-create",
        path = "/manager/cache-create",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON
)
public class CacheCreateAction extends CacheAction<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "n",
            required = true,
            description = "Cache name",
            displayOrder = 1
    )
    private String name;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "k",
            defaultValue = "java.lang.String",
            description = "Key type, default value is 'java.lang.String'.",
            displayOrder = 2
    )
    private String keyType;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "v",
            defaultValue = "java.lang.Object",
            description = "Value type, default value is 'java.lang.Object'.",
            displayOrder = 3

    )
    private String valueType;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "r",
            description = "Is read through.",
            displayOrder = 4
    )
    private boolean isReadThrough;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "w",
            description = "Is write through",
            displayOrder = 4
    )
    private boolean isWriteThrough;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "x",
            description = "Is statistics enabled."
    )
    private boolean isStatisticsEnabled;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "b",
            description = "Is store by value."
    )
    private boolean isStoreByValue;

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            option = "m",
            description = "Is management enabled."
    )
    private boolean isManagementEnabled;

    @Override
    public String execute() throws Exception {
        Cache cache = null;
        try {
            //configure the cache
            MutableConfiguration config =
                    new MutableConfiguration<Object, Object>()
                            .setTypes(Object.class, Object.class)
                            .setExpiryPolicyFactory(
                                    CreatedExpiryPolicy.factoryOf(
                                            new Duration(TimeUnit.MILLISECONDS, 60 * 1000)
                                    )
                            )
                            .setStoreByValue(false)
                            .setManagementEnabled(true)
                            .setStatisticsEnabled(true);
            cache = cacheManager.createCache(name, config);

        } catch (CacheException e) {
            // concurrent cache initialization
            cache = cacheManager.getCache(name);
        }

        return null;
    }
}
