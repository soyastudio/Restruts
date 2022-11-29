package soya.framework.cache.actions;

import soya.framework.action.Action;
import soya.framework.action.WiredService;

import javax.cache.CacheManager;

public abstract class CacheAction<T> extends Action<T> {

    @WiredService
    protected CacheManager cacheManager;
}
