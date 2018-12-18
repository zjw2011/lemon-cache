package org.lemonframework.cache.redis;

import org.lemonframework.cache.external.ExternalCacheConfig;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * jedis cache config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedisCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    private Pool<Jedis> jedisPool;

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }
}
