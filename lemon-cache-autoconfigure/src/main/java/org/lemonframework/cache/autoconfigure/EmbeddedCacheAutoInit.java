package org.lemonframework.cache.autoconfigure;

import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.anno.CacheConsts;
import org.lemonframework.cache.embedded.EmbeddedCacheBuilder;

/**
 * embedded cache autoinit.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class EmbeddedCacheAutoInit extends AbstractCacheAutoInit {

    public EmbeddedCacheAutoInit(String... cacheTypes) {
        super(cacheTypes);
    }

    @Override
    protected void parseGeneralConfig(CacheBuilder builder, ConfigTree ct) {
        super.parseGeneralConfig(builder, ct);
        EmbeddedCacheBuilder ecb = (EmbeddedCacheBuilder) builder;

        ecb.limit(Integer.parseInt(ct.getProperty("limit", String.valueOf(CacheConsts.DEFAULT_LOCAL_LIMIT))));
    }
}
