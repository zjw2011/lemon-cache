package org.lemonframework.cache.redisson;

import com.alicp.jetcache.external.ExternalCacheBuilder;
import org.redisson.api.RedissonClient;

/**
 * redisson builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedissonCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {
    public static class RedissonCacheBuilderImpl extends RedissonCacheBuilder<RedissonCacheBuilderImpl> {
    }

    public static RedissonCacheBuilderImpl createRedisCacheBuilder() {
        return new RedissonCacheBuilderImpl();
    }

    protected RedissonCacheBuilder() {
        buildFunc(config -> new RedissonCache((RedissonCacheConfig) config));
    }

    @Override
    public RedissonCacheConfig getConfig() {
        if (config == null) {
            config = new RedissonCacheConfig();
        }
        return (RedissonCacheConfig) config;
    }

    public T redisClient(RedissonClient redissonClient){
        getConfig().setRedissonClient(redissonClient);
        return self();
    }

    public void setRedisClient(RedissonClient redisClient) {
        getConfig().setRedissonClient(redisClient);
    }

    public T asyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        getConfig().setAsyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
        return self();
    }

    public void setAsyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        getConfig().setAsyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
    }
}
