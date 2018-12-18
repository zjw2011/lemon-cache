package org.lemonframework.cache.redis;

import org.lemonframework.cache.external.ExternalCacheBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

/**
 * jedis cache builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedisCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {
    public static class RedisCacheBuilderImpl extends RedisCacheBuilder<RedisCacheBuilderImpl> {
    }

    public static RedisCacheBuilderImpl createRedisCacheBuilder() {
        return new RedisCacheBuilderImpl();
    }

    protected RedisCacheBuilder() {
        buildFunc(config -> new RedisCache((RedisCacheConfig) config));
    }

    @Override
    public RedisCacheConfig getConfig() {
        if (config == null) {
            config = new RedisCacheConfig();
        }
        return (RedisCacheConfig) config;
    }

    public T jedisPool(Pool<Jedis> pool){
        getConfig().setJedisPool(pool);
        return self();
    }

    public void setJedisPool(JedisPool jedisPool) {
        getConfig().setJedisPool(jedisPool);
    }

}
