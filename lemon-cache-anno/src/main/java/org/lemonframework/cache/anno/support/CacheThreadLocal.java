package org.lemonframework.cache.anno.support;

/**
 * cache thread local.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
class CacheThreadLocal {

    private int enabledCount = 0;

    int getEnabledCount() {
        return enabledCount;
    }

    void setEnabledCount(int enabledCount) {
        this.enabledCount = enabledCount;
    }
}
