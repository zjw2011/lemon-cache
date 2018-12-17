package org.lemonframework.cache.external;

import java.io.IOException;

import org.lemonframework.cache.AbstractCache;
import org.lemonframework.cache.CacheConfigException;
import org.lemonframework.cache.CacheException;

/**
 * cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class AbstractExternalCache<K, V> extends AbstractCache<K, V> {

    private ExternalCacheConfig<K, V> config;

    public AbstractExternalCache(ExternalCacheConfig<K, V> config) {
        this.config = config;
        checkConfig();
    }

    protected void checkConfig() {
        if (config.getValueEncoder() == null) {
            throw new CacheConfigException("no value encoder");
        }
        if (config.getValueDecoder() == null) {
            throw new CacheConfigException("no value decoder");
        }
        if (config.getKeyPrefix() == null){
            throw new CacheConfigException("keyPrefix is required");
        }
    }

    public byte[] buildKey(K key) {
        try {
            Object newKey = key;
            if (key instanceof byte[]) {
                newKey = key;
            } else if(key instanceof String){
                newKey = key;
            } else {
                if (config.getKeyConvertor() != null) {
                    newKey = config.getKeyConvertor().apply(key);
                }
            }
            return ExternalKeyUtil.buildKeyAfterConvert(newKey, config.getKeyPrefix());
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

}
