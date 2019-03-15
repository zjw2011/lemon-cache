package org.lemonframework.cache.autoconfigure.redisson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.autoconfigure.AutoConfigureBeans;
import com.alicp.jetcache.autoconfigure.ConfigTree;
import com.alicp.jetcache.autoconfigure.ExternalCacheAutoInit;
import com.alicp.jetcache.autoconfigure.JetCacheCondition;
import com.alicp.jetcache.external.ExternalCacheBuilder;
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

    public static class RedissonCondition extends JetCacheCondition {
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

            long asyncResultTimeoutInMillis = Long.parseLong(
                    ct.getProperty("asyncResultTimeoutInMillis", Long.toString(CacheConsts.ASYNC_RESULT_TIMEOUT.toMillis())));

            ExternalCacheBuilder externalCacheBuilder = RedissonCacheBuilder.createRedisCacheBuilder()
                    .redisClient(this.redissonClient)
                    .asyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
            parseGeneralConfig(externalCacheBuilder, ct);

            // eg: "remote.default.client"
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".client", this.redissonClient);

            return externalCacheBuilder;
        }

    }


}