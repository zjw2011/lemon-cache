package org.lemonframework.cache.autoconfigure;

import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.anno.CacheConsts;
import org.lemonframework.cache.external.ExternalCacheBuilder;

/**
 * external cache autoinit.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class ExternalCacheAutoInit extends AbstractCacheAutoInit {
    public ExternalCacheAutoInit(String... cacheTypes) {
        super(cacheTypes);
    }

    @Override
    protected void parseGeneralConfig(CacheBuilder builder, ConfigTree ct) {
        super.parseGeneralConfig(builder, ct);
        ExternalCacheBuilder ecb = (ExternalCacheBuilder) builder;
        ecb.setKeyPrefix(ct.getProperty("keyPrefix"));
        ecb.setValueEncoder(configProvider.parseValueEncoder(ct.getProperty("valueEncoder", CacheConsts.DEFAULT_SERIAL_POLICY)));
        ecb.setValueDecoder(configProvider.parseValueDecoder(ct.getProperty("valueDecoder", CacheConsts.DEFAULT_SERIAL_POLICY)));
    }
}
