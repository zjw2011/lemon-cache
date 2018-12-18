package org.lemonframework.cache.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.embedded.LinkedHashMapCacheBuilder;

/**
 * linked hashmap auto config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Component
@Conditional(LinkedHashMapAutoConfiguration.LinkedHashMapCondition.class)
public class LinkedHashMapAutoConfiguration extends EmbeddedCacheAutoInit {
    public LinkedHashMapAutoConfiguration() {
        super("linkedhashmap");
    }

    @Override
    protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
        LinkedHashMapCacheBuilder builder = LinkedHashMapCacheBuilder.createLinkedHashMapCacheBuilder();
        parseGeneralConfig(builder, ct);
        return builder;
    }

    public static class LinkedHashMapCondition extends LemonCacheCondition {
        public LinkedHashMapCondition() {
            super("linkedhashmap");
        }
    }
}
