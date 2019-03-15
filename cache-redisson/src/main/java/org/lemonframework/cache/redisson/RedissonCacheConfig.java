package org.lemonframework.cache.redisson;

import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.external.ExternalCacheConfig;
import org.redisson.api.RedissonClient;

/**
 * redisson cache config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedissonCacheConfig<K, V> extends ExternalCacheConfig<K, V> {
    private RedissonClient redissonClient;

    private long asyncResultTimeoutInMillis = CacheConsts.ASYNC_RESULT_TIMEOUT.toMillis();

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public long getAsyncResultTimeoutInMillis() {
        return asyncResultTimeoutInMillis;
    }

    public void setAsyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        this.asyncResultTimeoutInMillis = asyncResultTimeoutInMillis;
    }
}
