package org.lemonframework.cache;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lemonframework.cache.event.CacheEvent;
import org.lemonframework.cache.event.CacheLoadAllEvent;
import org.lemonframework.cache.event.CacheLoadEvent;

/**
 * util.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
class CacheUtil {

    public static <K, V> CacheLoader<K, V> createProxyLoader(Cache<K, V> cache,
                                                             CacheLoader<K, V> loader,
                                                             Consumer<CacheEvent> eventConsumer) {
        return new CacheLoader<K, V>() {
            @Override
            public V load(K key) throws Throwable {
                long t = System.currentTimeMillis();
                V v = null;
                boolean success = false;
                try {
                    v = loader.load(key);
                    success = true;
                } finally {
                    t = System.currentTimeMillis() - t;
                    CacheLoadEvent event = new CacheLoadEvent(cache, t, key, v, success);
                    eventConsumer.accept(event);
                }
                return v;
            }

            @Override
            public Map<K, V> loadAll(Set<K> keys) throws Throwable {
                long t = System.currentTimeMillis();
                boolean success = false;
                Map<K, V> kvMap = null;
                try {
                    kvMap = loader.loadAll(keys);
                    success = true;
                } finally {
                    t = System.currentTimeMillis() - t;
                    CacheLoadAllEvent event = new CacheLoadAllEvent(cache, t, keys, kvMap, success);
                    eventConsumer.accept(event);
                }
                return kvMap;
            }

            @Override
            public boolean vetoCacheUpdate() {
                return loader.vetoCacheUpdate();
            }
        };
    }

    public static <K, V> Function<K, V> createProxyLoader(Cache<K, V> cache,
                                                          Function<K, V> loader,
                                                          Consumer<CacheEvent> eventConsumer) {
        return (k) -> {
            long t = System.currentTimeMillis();
            V v = null;
            boolean success = false;
            try {
                v = loader.apply(k);
                success = true;
            } finally {
                t = System.currentTimeMillis() - t;
                CacheLoadEvent event = new CacheLoadEvent(cache, t, k, v, success);
                eventConsumer.accept(event);
            }
            return v;
        };
    }


    public static <K, V> AbstractCache<K, V> getAbstractCache(Cache<K, V> c) {
        while (c instanceof ProxyCache) {
            c = ((ProxyCache) c).getTargetCache();
        }
        return (AbstractCache) c;
    }

}
