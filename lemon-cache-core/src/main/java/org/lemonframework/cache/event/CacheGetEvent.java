package org.lemonframework.cache.event;

import org.lemonframework.cache.Cache;
import org.lemonframework.cache.CacheGetResult;

/**
 * event.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheGetEvent extends CacheEvent {
    private long millis;
    private Object key;
    private CacheGetResult result;

    public CacheGetEvent(Cache cache, long millis, Object key, CacheGetResult result) {
        super(cache);
        this.millis = millis;
        this.key = key;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Object getKey() {
        return key;
    }

    public CacheGetResult getResult() {
        return result;
    }
}
