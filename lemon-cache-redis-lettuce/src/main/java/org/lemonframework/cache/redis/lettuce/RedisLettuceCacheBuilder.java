package org.lemonframework.cache.redis.lettuce;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;
import org.lemonframework.cache.external.ExternalCacheBuilder;

/**
 * lettuce cache builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedisLettuceCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {
    public static class RedisLettuceCacheBuilderImpl extends RedisLettuceCacheBuilder<RedisLettuceCacheBuilderImpl> {
    }

    public static RedisLettuceCacheBuilderImpl createRedisLettuceCacheBuilder() {
        return new RedisLettuceCacheBuilderImpl();
    }

    protected RedisLettuceCacheBuilder() {
        buildFunc(config -> new RedisLettuceCache((RedisLettuceCacheConfig) config));
    }

    @Override
    public RedisLettuceCacheConfig getConfig() {
        if (config == null) {
            config = new RedisLettuceCacheConfig();
        }
        return (RedisLettuceCacheConfig) config;
    }

    public T redisClient(AbstractRedisClient redisClient){
        getConfig().setRedisClient(redisClient);
        return self();
    }

    public void setRedisClient(AbstractRedisClient redisClient) {
        getConfig().setRedisClient(redisClient);
    }

    public T connection(StatefulConnection connection) {
        getConfig().setConnection(connection);
        return self();
    }

    public void setConnection(StatefulConnection connection) {
        getConfig().setConnection(connection);
    }

    public T asyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        getConfig().setAsyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
        return self();
    }

    public void setAsyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        getConfig().setAsyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
    }
}
