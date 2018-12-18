package org.lemonframework.cache.anno.support;

/**
 * cache invalidate anno config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheInvalidateAnnoConfig extends CacheAnnoConfig {
    private boolean multi;

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
