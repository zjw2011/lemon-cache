package org.lemonframework.cache.anno.method;

import org.springframework.context.ApplicationContext;

/**
 * spring cache invoker context.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class SpringCacheInvokeContext extends CacheInvokeContext {
    protected ApplicationContext context;

    public SpringCacheInvokeContext(ApplicationContext context) {
        this.context = context;
    }

    public Object bean(String name) {
        return context.getBean(name);
    }


}
