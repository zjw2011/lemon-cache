package org.lemonframework.cache.embedded;

/**
 * cache builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CaffeineCacheBuilder<T extends EmbeddedCacheBuilder<T>> extends EmbeddedCacheBuilder<T> {
    public static class CaffeineCacheBuilderImpl extends CaffeineCacheBuilder<CaffeineCacheBuilderImpl> {
    }

    public static CaffeineCacheBuilderImpl createCaffeineCacheBuilder() {
        return new CaffeineCacheBuilderImpl();
    }

    protected CaffeineCacheBuilder() {
        buildFunc((c) -> new CaffeineCache((EmbeddedCacheConfig) c));
    }
}
