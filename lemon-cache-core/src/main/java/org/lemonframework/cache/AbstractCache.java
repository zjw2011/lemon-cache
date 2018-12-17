package org.lemonframework.cache;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lemonframework.cache.embedded.AbstractEmbeddedCache;
import org.lemonframework.cache.event.CacheEvent;
import org.lemonframework.cache.event.CacheGetAllEvent;
import org.lemonframework.cache.event.CacheGetEvent;
import org.lemonframework.cache.event.CachePutAllEvent;
import org.lemonframework.cache.event.CachePutEvent;
import org.lemonframework.cache.event.CacheRemoveAllEvent;
import org.lemonframework.cache.event.CacheRemoveEvent;
import org.lemonframework.cache.external.AbstractExternalCache;

/**
 * cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    private static Logger logger = LoggerFactory.getLogger(AbstractCache.class);

    private ConcurrentHashMap<Object, LoaderLock> loaderMap;

    ConcurrentHashMap<Object, LoaderLock> initOrGetLoaderMap() {
        if (loaderMap == null) {
            synchronized (this) {
                if (loaderMap == null) {
                    loaderMap = new ConcurrentHashMap<>();
                }
            }
        }
        return loaderMap;
    }

    public void notify(CacheEvent e) {
        List<CacheMonitor> monitors = config().getMonitors();
        for (CacheMonitor m : monitors) {
            m.afterOperation(e);
        }
    }

    @Override
    public final CacheGetResult<V> GET(K key) {
        long t = System.currentTimeMillis();
        CacheGetResult<V> result = do_GET(key);
        result.future().thenRun(() -> {
            CacheGetEvent event = new CacheGetEvent(this, System.currentTimeMillis() - t, key, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheGetResult<V> do_GET(K key);

    @Override
    public final MultiGetResult<K, V> GET_ALL(Set<? extends K> keys) {
        long t = System.currentTimeMillis();
        MultiGetResult<K, V> result = do_GET_ALL(keys);
        result.future().thenRun(() -> {
            CacheGetAllEvent event = new CacheGetAllEvent(this, System.currentTimeMillis() - t, keys, result);
            notify(event);
        });
        return result;
    }

    protected abstract MultiGetResult<K, V> do_GET_ALL(Set<? extends K> keys);

    @Override
    public final V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull) {
        return computeIfAbsentImpl(key, loader, cacheNullWhenLoaderReturnNull,
                0, null, this);
    }

    @Override
    public final V computeIfAbsent(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull,
                                   long expireAfterWrite, TimeUnit timeUnit) {
        return computeIfAbsentImpl(key, loader, cacheNullWhenLoaderReturnNull,
                expireAfterWrite, timeUnit, this);
    }

    private static <K, V> boolean needUpdate(V loadedValue, boolean cacheNullWhenLoaderReturnNull, Function<K, V> loader) {
        if (loadedValue == null && !cacheNullWhenLoaderReturnNull) {
            return false;
        }
        if (loader instanceof CacheLoader && ((CacheLoader<K, V>) loader).vetoCacheUpdate()) {
            return false;
        }
        return true;
    }

    static <K, V> V computeIfAbsentImpl(K key, Function<K, V> loader, boolean cacheNullWhenLoaderReturnNull,
                                        long expireAfterWrite, TimeUnit timeUnit, Cache<K, V> cache) {
        AbstractCache<K, V> abstractCache = CacheUtil.getAbstractCache(cache);
        Function<K, V> newLoader = CacheUtil.createProxyLoader(cache, loader, abstractCache::notify);
        CacheGetResult<V> r = cache.GET(key);
        if (r.isSuccess()) {
            return r.getValue();
        } else {
            Consumer<V> cacheUpdater = (loadedValue) -> {
                if(needUpdate(loadedValue, cacheNullWhenLoaderReturnNull, loader)) {
                    if (timeUnit != null) {
                        cache.PUT(key, loadedValue, expireAfterWrite, timeUnit);
                    } else {
                        cache.put(key, loadedValue);
                    }
                }
            };

            V loadedValue;
            if (cache.config().isCachePenetrationProtect()) {
                ConcurrentHashMap<Object, LoaderLock> loaderMap = abstractCache.initOrGetLoaderMap();
                loadedValue = synchronizedLoad(abstractCache, key, newLoader, cacheUpdater, loaderMap);
            } else {
                loadedValue = newLoader.apply(key);
                cacheUpdater.accept(loadedValue);
            }

            return loadedValue;
        }
    }

    static <K, V> V synchronizedLoad(Cache<K,V> abstractCache, K key, Function<K, V> newLoader,
                                     Consumer<V> cacheUpdater,
                                     ConcurrentHashMap<Object, LoaderLock> loaderMap) {
        V loadedValue;
        Object lockKey = buildLoaderLockKey(abstractCache, key);
        while (true) {
            boolean create[] = new boolean[1];
            LoaderLock ll = loaderMap.computeIfAbsent(lockKey, (unusedKey) -> {
                create[0] = true;
                LoaderLock loaderLock = new LoaderLock();
                return loaderLock;
            });
            if (create[0]) {
                try {
                    loadedValue = newLoader.apply(key);
                    ll.success = true;
                    ll.value = loadedValue;
                    cacheUpdater.accept(loadedValue);
                    break;
                } finally {
                    loaderMap.remove(lockKey);
                    ll.signal.countDown();
                }
            } else {
                try {
                    ll.signal.await();
                    if (ll.success) {
                        loadedValue = (V) ll.value;
                        break;
                    } else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    throw new CacheException("loader wait interrupted", e);
                }
            }
        }
        return loadedValue;
    }

    private static Object buildLoaderLockKey(Cache c, Object key) {
        if (c instanceof AbstractEmbeddedCache) {
            return ((AbstractEmbeddedCache) c).buildKey(key);
        } else if (c instanceof AbstractExternalCache) {
            byte bytes[] = ((AbstractExternalCache) c).buildKey(key);
            return ByteBuffer.wrap(bytes);
        } else if (c instanceof MultiLevelCache) {
            c = ((MultiLevelCache) c).caches()[0];
            return buildLoaderLockKey(c, key);
        } else if(c instanceof ProxyCache) {
            c = ((ProxyCache) c).getTargetCache();
            return buildLoaderLockKey(c, key);
        } else {
            throw new CacheException("impossible");
        }
    }

    @Override
    public final CacheResult PUT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        long t = System.currentTimeMillis();
        CacheResult result = do_PUT(key, value, expireAfterWrite, timeUnit);
        result.future().thenRun(() -> {
            CachePutEvent event = new CachePutEvent(this, System.currentTimeMillis() - t, key, value, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheResult do_PUT(K key, V value, long expireAfterWrite, TimeUnit timeUnit);

    @Override
    public final CacheResult PUT_ALL(Map<? extends K, ? extends V> map, long expireAfterWrite, TimeUnit timeUnit) {
        long t = System.currentTimeMillis();
        CacheResult result = do_PUT_ALL(map, expireAfterWrite, timeUnit);
        result.future().thenRun(() -> {
            CachePutAllEvent event = new CachePutAllEvent(this, System.currentTimeMillis() - t, map, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheResult do_PUT_ALL(Map<? extends K, ? extends V> map, long expireAfterWrite, TimeUnit timeUnit);

    @Override
    public final CacheResult REMOVE(K key) {
        long t = System.currentTimeMillis();
        CacheResult result = do_REMOVE(key);
        result.future().thenRun(() -> {
            CacheRemoveEvent event = new CacheRemoveEvent(this, System.currentTimeMillis() - t, key, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheResult do_REMOVE(K key);

    @Override
    public final CacheResult REMOVE_ALL(Set<? extends K> keys) {
        long t = System.currentTimeMillis();
        CacheResult result = do_REMOVE_ALL(keys);
        result.future().thenRun(() -> {
            CacheRemoveAllEvent event = new CacheRemoveAllEvent(this, System.currentTimeMillis() - t, keys, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheResult do_REMOVE_ALL(Set<? extends K> keys);

    @Override
    public final CacheResult PUT_IF_ABSENT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        long t = System.currentTimeMillis();
        CacheResult result = do_PUT_IF_ABSENT(key, value, expireAfterWrite, timeUnit);
        result.future().thenRun(() -> {
            CachePutEvent event = new CachePutEvent(this, System.currentTimeMillis() - t, key, value, result);
            notify(event);
        });
        return result;
    }

    protected abstract CacheResult do_PUT_IF_ABSENT(K key, V value, long expireAfterWrite, TimeUnit timeUnit);

    static class LoaderLock {
        CountDownLatch signal = new CountDownLatch(1);
        boolean success;
        Object value;
    }
}