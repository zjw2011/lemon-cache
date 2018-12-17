package org.lemonframework.cache;

/**
 * cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface ProxyCache<K, V> extends Cache<K, V> {
    Cache<K, V> getTargetCache();

    @Override
    default <T> T unwrap(Class<T> clazz) {
        return getTargetCache().unwrap(clazz);
    }

}