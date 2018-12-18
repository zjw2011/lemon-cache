package org.lemonframework.cache.autoconfigure;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lemonframework.cache.CacheBuilder;

/**
 * beans.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class AutoConfigureBeans {

    private Map<String, CacheBuilder> localCacheBuilders = new HashMap<>();

    private Map<String, CacheBuilder> remoteCacheBuilders = new HashMap<>();

    private Map<String, Object> customContainer = Collections.synchronizedMap(new HashMap<>());

    public Map<String, CacheBuilder> getLocalCacheBuilders() {
        return localCacheBuilders;
    }

    public void setLocalCacheBuilders(Map<String, CacheBuilder> localCacheBuilders) {
        this.localCacheBuilders = localCacheBuilders;
    }

    public Map<String, CacheBuilder> getRemoteCacheBuilders() {
        return remoteCacheBuilders;
    }

    public void setRemoteCacheBuilders(Map<String, CacheBuilder> remoteCacheBuilders) {
        this.remoteCacheBuilders = remoteCacheBuilders;
    }

    public Map<String, Object> getCustomContainer() {
        return customContainer;
    }

    public void setCustomContainer(Map<String, Object> customContainer) {
        this.customContainer = customContainer;
    }
}
