package org.lemonframework.cache;

import org.lemonframework.cache.event.CacheEvent;

/**
 * monitor.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@FunctionalInterface
public interface CacheMonitor {

    void afterOperation(CacheEvent event);

}