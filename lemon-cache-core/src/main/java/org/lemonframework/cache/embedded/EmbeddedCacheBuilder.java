package org.lemonframework.cache.embedded;

import org.lemonframework.cache.AbstractCacheBuilder;

/**
 * build.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class EmbeddedCacheBuilder<T extends EmbeddedCacheBuilder<T>> extends AbstractCacheBuilder<T> {

    public EmbeddedCacheBuilder(){
    }

    public static class EmbeddedCacheBuilderImpl extends EmbeddedCacheBuilder<EmbeddedCacheBuilderImpl> {
    }

    public static EmbeddedCacheBuilderImpl createEmbeddedCacheBuilder(){
        return new EmbeddedCacheBuilderImpl();
    }

    @Override
    public EmbeddedCacheConfig getConfig() {
        if (config == null) {
            config = new EmbeddedCacheConfig();
        }
        return (EmbeddedCacheConfig) config;
    }

    public T limit(int limit){
        getConfig().setLimit(limit);
        return self();
    }

    public void setLimit(int limit){
        getConfig().setLimit(limit);
    }

}
