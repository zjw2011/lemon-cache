package org.lemonframework.cache.redisson;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.lemonframework.cache.CacheConfig;
import org.lemonframework.cache.CacheConfigException;
import org.lemonframework.cache.CacheGetResult;
import org.lemonframework.cache.CacheResult;
import org.lemonframework.cache.CacheResultCode;
import org.lemonframework.cache.CacheValueHolder;
import org.lemonframework.cache.MultiGetResult;
import org.lemonframework.cache.external.AbstractExternalCache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedissonCache<K, V> extends AbstractExternalCache<K, V> {

    private RedissonCacheConfig<K, V> config;

    private RedissonClient redissonClient;

    public RedissonCache(RedissonCacheConfig<K, V> config) {
        super(config);
        this.config = config;
        this.redissonClient = config.getRedissonClient();

        if (redissonClient == null) {
            throw new CacheConfigException("no redissonClient");
        }
    }

    @Override
    protected CacheGetResult<V> do_GET(K key) {
        if (key == null) {
            return new CacheGetResult<V>(CacheResultCode.FAIL, CacheResult.MSG_ILLEGAL_ARGUMENT, null);
        }

        try {
            byte[] newKey = buildKey(key);
            String newStringKey = new String(newKey, "UTF-8");
            RBucket<V> rBucket = redissonClient.getBucket(newStringKey);
            CacheValueHolder<V> holder = new CacheValueHolder<V>(rBucket.get(), config.getExpireAfterWriteInMillis());
            if (System.currentTimeMillis() >= holder.getExpireTime()) {
                return CacheGetResult.EXPIRED_WITHOUT_MSG;
            }
            return new CacheGetResult(CacheResultCode.SUCCESS, null, holder);

        } catch (Exception ex) {
            logError("GET", key, ex);
            return new CacheGetResult(ex);
        }
    }

    @Override
    protected MultiGetResult<K, V> do_GET_ALL(Set<? extends K> keys) {
        return null;
    }

    @Override
    protected CacheResult do_PUT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    protected CacheResult do_PUT_ALL(Map<? extends K, ? extends V> map, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    protected CacheResult do_REMOVE(K key) {
        return null;
    }

    @Override
    protected CacheResult do_REMOVE_ALL(Set<? extends K> keys) {
        return null;
    }

    @Override
    protected CacheResult do_PUT_IF_ABSENT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.equals(RedissonClient.class)) {
            return (T) redissonClient;
        }
        throw new IllegalArgumentException(clazz.getName());
    }

    @Override
    public CacheConfig<K, V> config() {
        return config;
    }
}
