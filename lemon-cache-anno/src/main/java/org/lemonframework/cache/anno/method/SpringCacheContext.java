package org.lemonframework.cache.anno.method;

import org.springframework.context.ApplicationContext;

import org.lemonframework.cache.anno.support.CacheContext;
import org.lemonframework.cache.anno.support.GlobalCacheConfig;

/**
 * spring cache context.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class SpringCacheContext extends CacheContext {

    private ApplicationContext applicationContext;

    public SpringCacheContext(GlobalCacheConfig globalCacheConfig, ApplicationContext applicationContext) {
        super(globalCacheConfig);
        this.applicationContext = applicationContext;
    }

    @Override
    protected CacheInvokeContext newCacheInvokeContext() {
        return new SpringCacheInvokeContext(applicationContext);
    }
}
