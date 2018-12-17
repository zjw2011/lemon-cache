package org.lemonframework.cache.event;

import java.util.Set;

import org.lemonframework.cache.Cache;
import org.lemonframework.cache.CacheResult;

/**
 * Created on 2017/2/22.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CacheRemoveAllEvent extends CacheEvent {
    private final long millis;
    private final Set keys;
    private final CacheResult result;

    public CacheRemoveAllEvent(Cache cache, long millis, Set keys, CacheResult result) {
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

    public CacheResult getResult() {
        return result;
    }
}
