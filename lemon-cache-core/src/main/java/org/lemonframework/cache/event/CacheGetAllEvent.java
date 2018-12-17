package org.lemonframework.cache.event;

import java.util.Set;

import org.lemonframework.cache.Cache;
import org.lemonframework.cache.MultiGetResult;

/**
 * event.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheGetAllEvent extends CacheEvent {
    private final long millis;
    private final Set keys;
    private final MultiGetResult result;

    public CacheGetAllEvent(Cache cache, long millis, Set keys, MultiGetResult result) {
        super(cache);
        this.millis = millis;
        this.keys = keys;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Set getKeys() {
        return keys;
    }

    public MultiGetResult getResult() {
        return result;
    }
}