package org.lemonframework.cache.embedded;

import org.lemonframework.cache.CacheConfig;

/**
 * config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class EmbeddedCacheConfig<K, V> extends CacheConfig<K, V> {
    private int limit = 100;//CacheConsts.DEFAULT_LOCAL_LIMIT;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
