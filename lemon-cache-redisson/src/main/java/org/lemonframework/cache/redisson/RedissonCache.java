package org.lemonframework.cache.redisson;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.lemonframework.cache.CacheConfig;
import org.lemonframework.cache.CacheConfigException;
import org.lemonframework.cache.CacheGetResult;
import org.lemonframework.cache.CacheResult;
import org.lemonframework.cache.CacheResultCode;
import org.lemonframework.cache.CacheValueHolder;
import org.lemonframework.cache.MultiGetResult;
import org.lemonframework.cache.ResultData;
import org.lemonframework.cache.external.AbstractExternalCache;
import org.lemonframework.cache.support.LemonCacheExecutor;
import org.redisson.api.BatchOptions;
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RBucketAsync;
import org.redisson.api.RBuckets;
import org.redisson.api.RFuture;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;

/**
 * redisson cache.
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

        if (config.isExpireAfterAccess()) {
            throw new CacheConfigException("expireAfterAccess is not supported");
        }
    }

    private void setTimeout(CacheResult cr) {
        Duration d = Duration.ofMillis(config.getAsyncResultTimeoutInMillis());
        cr.setTimeout(d);
    }

    private String buildStringKey(K key) {
        if (key == null) {
            return null;
        }
        try {
            final byte[] bytes = buildKey(key);
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LemonCacheExecutor.defaultExecutor().execute(() -> logError("buildKey", key, ex));
            return null;
        }

    }

    @Override
    protected CacheGetResult<V> do_GET(K key) {
        String newStringKey = buildStringKey(key);

        if (newStringKey == null) {
            return new CacheGetResult<V>(CacheResultCode.FAIL, CacheResult.MSG_ILLEGAL_ARGUMENT, null);
        }

        RBucket<V> rBucket = redissonClient.getBucket(newStringKey);
        final RFuture<V> future = rBucket.getAsync();

        CacheGetResult result = new CacheGetResult(future.handle((valueBytes, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("GET", key, ex));
                return new ResultData(ex);
            } else {
                if (valueBytes != null) {
                    CacheValueHolder<V> holder = new CacheValueHolder<V>(valueBytes, config.getExpireAfterWriteInMillis());
                    if (System.currentTimeMillis() >= holder.getExpireTime()) {
                        return new ResultData(CacheResultCode.EXPIRED, null, null);
                    } else {
                        return new ResultData(CacheResultCode.SUCCESS, null, holder);
                    }
                } else {
                    return new ResultData(CacheResultCode.NOT_EXISTS, null, null);
                }
            }
        }));
        setTimeout(result);
        return result;
    }

    @Override
    protected MultiGetResult<K, V> do_GET_ALL(Set<? extends K> keys) {

        ArrayList<K> keyList = new ArrayList<K>(keys);

        String[] newKeys = keyList.stream().map((k) -> buildStringKey(k))
                .filter(k -> (k != null))
                .toArray(String[]::new);

        Map<K, CacheGetResult<V>> resultMap = new HashMap<>();
        if (newKeys.length == 0) {
            return new MultiGetResult<K, V>(CacheResultCode.SUCCESS, null, resultMap);
        }

        final RBuckets buckets = redissonClient.getBuckets();
        final RFuture<Map<String, V>> future = buckets.getAsync(newKeys);

        MultiGetResult result = new MultiGetResult<K, V>(future.handle((map, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("GET_ALL", "keys(" + keys.size() + ")", ex));
                return new ResultData(ex);
            } else {
                int keysLen = keyList.size();
                for (int i = 0; i < keysLen; i++) {
                    K ramKey = keyList.get(i);
                    String key = buildStringKey(ramKey);
                    if (key == null) {
                        continue;
                    }
                    CacheValueHolder<V> holder = new CacheValueHolder<V>(map.get(key), config.getExpireAfterWriteInMillis());

                    CacheGetResult<V> r = new CacheGetResult<V>(CacheResultCode.SUCCESS, null, holder);

                    resultMap.put(ramKey, r);
                }
                return new ResultData(CacheResultCode.SUCCESS, null, resultMap);
            }
        }));
        setTimeout(result);
        return result;
    }

    @Override
    protected CacheResult do_PUT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        String newStringKey = buildStringKey(key);
        if (newStringKey == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        RBucket<V> rBucket = redissonClient.getBucket(newStringKey);
        final RFuture<Void> future = rBucket.setAsync(value, expireAfterWrite, timeUnit);
        CacheResult result = new CacheResult(future.handle((rt, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("PUT", key, ex));
                return new ResultData(ex);
            } else {
                return new ResultData(CacheResultCode.SUCCESS, null, null);
            }
        }));
        setTimeout(result);
        return result;

    }

    @Override
    protected CacheResult do_PUT_ALL(Map<? extends K, ? extends V> map, long expireAfterWrite, TimeUnit timeUnit) {
        if (map == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        BatchOptions batchOptions = BatchOptions.defaults()
                .executionMode(BatchOptions.ExecutionMode.IN_MEMORY);

        RBatch batch = redissonClient.createBatch(batchOptions);
        for (Map.Entry<? extends K, ? extends V> en : map.entrySet()) {
            String newStringKey = buildStringKey(en.getKey());
            if (newStringKey == null) {
                return CacheResult.FAIL_WITHOUT_MSG;
            }
            final RBucketAsync<V> bucket = batch.getBucket(newStringKey);
            bucket.setAsync(en.getValue(), expireAfterWrite, timeUnit);
        }
        final RFuture<BatchResult<?>> batchResultRFuture = batch.executeAsync();
        CacheResult result = new CacheResult(batchResultRFuture.handle((batchResult, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("PUT_ALL", "map(" + map.size() + ")", ex));
                return new ResultData(ex);
            } else {
                return new ResultData(CacheResultCode.SUCCESS, null, null);
            }
        }));
        setTimeout(result);
        return result;
    }

    @Override
    protected CacheResult do_REMOVE(K key) {

        String newStringKey = buildStringKey(key);
        if (newStringKey == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }

        RBucket<V> rBucket = redissonClient.getBucket(newStringKey);
        final RFuture<Boolean> future = rBucket.deleteAsync();

        CacheResult result = new CacheResult(future.handle((success, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("REMOVE", key, ex));
                return new ResultData(ex);
            } else {
                CacheResultCode resultCode = (success.booleanValue()) ? CacheResultCode.SUCCESS : CacheResultCode.FAIL;
                return new ResultData(resultCode, null, null);
            }
        }));
        setTimeout(result);
        return result;

    }

    @Override
    protected CacheResult do_REMOVE_ALL(Set<? extends K> keys) {
        ArrayList<K> keyList = new ArrayList<K>(keys);
        String[] newKeys = keyList.stream().map((k) -> buildStringKey(k))
                .filter(k -> (k != null))
                .toArray(String[]::new);
        if (newKeys.length == 0) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }
        final RKeys rKeys = redissonClient.getKeys();
        final RFuture<Long> future = rKeys.deleteAsync(newKeys);

        CacheResult result = new CacheResult(future.handle((size, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("REMOVE_ALL", "keys(" + keys.size() + ")", ex));
                return new ResultData(ex);
            } else {
                CacheResultCode resultCode = (size == newKeys.length) ? CacheResultCode.SUCCESS : CacheResultCode.PART_SUCCESS;
                return new ResultData(resultCode, null, null);
            }
        }));
        setTimeout(result);
        return result;

    }

    @Override
    protected CacheResult do_PUT_IF_ABSENT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        String newStringKey = buildStringKey(key);
        if (newStringKey == null) {
            return CacheResult.FAIL_ILLEGAL_ARGUMENT;
        }

        RBucket<V> rBucket = redissonClient.getBucket(newStringKey);
        final RFuture<Boolean> future = rBucket.trySetAsync(value, expireAfterWrite, timeUnit);
        CacheResult result = new CacheResult(future.handle((success, ex) -> {
            if (ex != null) {
                LemonCacheExecutor.defaultExecutor().execute(() -> logError("PUT_IF_ABSENT", key, ex));
                return new ResultData(ex);
            } else {
                CacheResultCode resultCode = (success.booleanValue()) ? CacheResultCode.SUCCESS : CacheResultCode.EXISTS;
                return new ResultData(resultCode, null, null);
            }
        }));
        setTimeout(result);
        return result;

    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        if (RedissonClient.class.isAssignableFrom(clazz)) {
            return (T) redissonClient;
        }
        throw new IllegalArgumentException(clazz.getName());
    }

    @Override
    public CacheConfig<K, V> config() {
        return config;
    }
}
