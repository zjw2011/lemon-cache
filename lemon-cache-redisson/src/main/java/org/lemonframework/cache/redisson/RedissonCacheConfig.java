package org.lemonframework.cache.redisson;

import org.lemonframework.cache.external.ExternalCacheConfig;
import org.redisson.api.RedissonClient;

/**
 * redisson cache config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedissonCacheConfig<K, V> extends ExternalCacheConfig<K, V> {
    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
