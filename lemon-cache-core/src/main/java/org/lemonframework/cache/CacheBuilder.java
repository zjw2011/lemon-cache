package org.lemonframework.cache;

/**
 * build.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface CacheBuilder {
    <K, V> Cache<K, V> buildCache();
}