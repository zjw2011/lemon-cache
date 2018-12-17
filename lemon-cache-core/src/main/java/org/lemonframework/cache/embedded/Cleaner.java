package org.lemonframework.cache.embedded;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lemonframework.cache.support.LemonCacheExecutor;

/**
 * clean.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
class Cleaner {

    static LinkedList<WeakReference<LinkedHashMapCache>> linkedHashMapCaches = new LinkedList<>();

    static {
        ScheduledExecutorService executorService = LemonCacheExecutor.defaultExecutor();
        executorService.scheduleWithFixedDelay(() -> run(), 60, 60, TimeUnit.SECONDS);
    }

    static void add(LinkedHashMapCache cache) {
        synchronized (linkedHashMapCaches) {
            linkedHashMapCaches.add(new WeakReference<>(cache));
        }
    }

    static void run() {
        synchronized (linkedHashMapCaches) {
            Iterator<WeakReference<LinkedHashMapCache>> it = linkedHashMapCaches.iterator();
            while (it.hasNext()) {
                WeakReference<LinkedHashMapCache> ref = it.next();
                LinkedHashMapCache c = ref.get();
                if (c == null) {
                    it.remove();
                } else {
                    c.cleanExpiredEntry();
                }
            }
        }
    }

}
