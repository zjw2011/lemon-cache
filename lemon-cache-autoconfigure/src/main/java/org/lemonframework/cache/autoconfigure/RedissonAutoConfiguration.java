package org.lemonframework.cache.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.external.ExternalCacheBuilder;
import org.lemonframework.cache.redisson.RedissonCacheBuilder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

/**
 * redisson auto configuration.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Configuration
@Conditional(RedissonAutoConfiguration.RedissonCondition.class)
public class RedissonAutoConfiguration {

    public static final String AUTO_INIT_BEAN_NAME = "redissonAutoInit";

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        return Redisson.create();
    }

    @Bean(name = AUTO_INIT_BEAN_NAME)
    @ConditionalOnBean(RedissonClient.class)
    public RedissonAutoInit redisAutoInit(RedissonClient redissonClient) {
        return new RedissonAutoInit(redissonClient);
    }

    public static class RedissonCondition extends LemonCacheCondition {
        public RedissonCondition() {
            super("redisson");
        }
    }

    public static class RedissonAutoInit extends ExternalCacheAutoInit {
        private RedissonClient redissonClient;

        public RedissonAutoInit(RedissonClient redissonClient) {
            super("redisson");
            this.redissonClient = redissonClient;
        }

        @Autowired
        private AutoConfigureBeans autoConfigureBeans;


        @Override
        protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {

            ExternalCacheBuilder externalCacheBuilder = RedissonCacheBuilder.createRedisCacheBuilder()
                    .redisClient(redissonClient);
            parseGeneralConfig(externalCacheBuilder, ct);

            // eg: "redisson.remote.default"
            autoConfigureBeans.getCustomContainer().put("ressionClient." + cacheAreaWithPrefix, redissonClient);

            return externalCacheBuilder;
        }

    }


}
