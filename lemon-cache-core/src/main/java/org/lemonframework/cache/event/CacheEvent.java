package org.lemonframework.cache.event;

import org.lemonframework.cache.Cache;

/**
 * event.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheEvent {

    protected Cache cache;

    public CacheEvent(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }
}
