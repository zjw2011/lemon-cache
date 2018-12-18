package org.lemonframework.cache.support;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created on 2017/5/3.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class LemonCacheExecutor {
    protected static ScheduledExecutorService defaultExecutor;
    protected static ScheduledExecutorService heavyIOExecutor;

    private static int threadCount;

    public static ScheduledExecutorService defaultExecutor() {
        if (defaultExecutor != null) {
            return defaultExecutor;
        }
        synchronized (LemonCacheExecutor.class) {
            if (defaultExecutor == null) {
                defaultExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                    Thread t = new Thread(r, "LemonCacheDefaultExecutor");
                    t.setDaemon(true);
                    return t;
                });
            }
        }
        return defaultExecutor;
    }

    public static ScheduledExecutorService heavyIOExecutor() {
        if (heavyIOExecutor != null) {
            return heavyIOExecutor;
        }
        synchronized (LemonCacheExecutor.class) {
            if (heavyIOExecutor == null) {
                heavyIOExecutor = Executors.newScheduledThreadPool(10, r -> {
                    Thread t = new Thread(r, "LemonCacheHeavyIOExecutor" + threadCount++);
                    t.setDaemon(true);
                    return t;
                });
            }
        }
        return heavyIOExecutor;
    }

    public static void setDefaultExecutor(ScheduledExecutorService executor) {
        LemonCacheExecutor.defaultExecutor = executor;
    }

    public static void setHeavyIOExecutor(ScheduledExecutorService heavyIOExecutor) {
        LemonCacheExecutor.heavyIOExecutor = heavyIOExecutor;
    }
}
