/**
 * Created on 2018/1/22.
 */
package org.lemonframework.cache.anno.support;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
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
