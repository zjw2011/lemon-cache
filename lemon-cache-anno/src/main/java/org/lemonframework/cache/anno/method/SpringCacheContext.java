package org.lemonframework.cache.anno.method;

import org.springframework.context.ApplicationContext;

import org.lemonframework.cache.anno.support.CacheContext;
import org.lemonframework.cache.anno.support.GlobalCacheConfig;

/**
 * Created on 2016/10/19.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
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
