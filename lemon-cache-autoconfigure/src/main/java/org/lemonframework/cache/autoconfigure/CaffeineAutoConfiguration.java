package org.lemonframework.cache.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.embedded.CaffeineCacheBuilder;

/**
 * caffeine auto config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Component
@Conditional(CaffeineAutoConfiguration.CaffeineCondition.class)
public class CaffeineAutoConfiguration extends EmbeddedCacheAutoInit {
    public CaffeineAutoConfiguration() {
        super("caffeine");
        System.out.println("CaffeineAutoConfiguration init");
    }

    @Override
    protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
        CaffeineCacheBuilder builder = CaffeineCacheBuilder.createCaffeineCacheBuilder();
        parseGeneralConfig(builder, ct);
        return builder;
    }

    public static class CaffeineCondition extends LemonCacheCondition {
        public CaffeineCondition() {
            super("caffeine");
        }
    }
}
