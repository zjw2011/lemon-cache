package org.lemonframework.cache.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.lemonframework.cache.anno.support.GlobalCacheConfig;
import org.lemonframework.cache.anno.support.SpringConfigProvider;

/**
 * auto configuration.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Configuration
@ConditionalOnClass(GlobalCacheConfig.class)
@ConditionalOnMissingBean(GlobalCacheConfig.class)
@EnableConfigurationProperties(LemonCacheProperties.class)
@Import({LinkedHashMapAutoConfiguration.class,
        CaffeineAutoConfiguration.class,
        RedisAutoConfiguration.class})
public class LemonCacheAutoConfiguration {

    public static final String GLOBAL_CACHE_CONFIG_NAME = "globalCacheConfig";

    private SpringConfigProvider _springConfigProvider = new SpringConfigProvider();

    private AutoConfigureBeans _autoConfigureBeans = new AutoConfigureBeans();

    private GlobalCacheConfig _globalCacheConfig;

    @Bean
    @ConditionalOnMissingBean
    public SpringConfigProvider springConfigProvider() {
        return _springConfigProvider;
    }

    @Bean
    public AutoConfigureBeans autoConfigureBeans() {
        return _autoConfigureBeans;
    }

    @Bean
    public static BeanDependencyManager beanDependencyManager(){
        return new BeanDependencyManager();
    }

    @Bean(name = GLOBAL_CACHE_CONFIG_NAME)
    public GlobalCacheConfig globalCacheConfig(SpringConfigProvider configProvider,
                                               AutoConfigureBeans autoConfigureBeans,
                                               LemonCacheProperties props) {
        if (_globalCacheConfig != null) {
            return _globalCacheConfig;
        }
        _globalCacheConfig = new GlobalCacheConfig();
        _globalCacheConfig.setConfigProvider(configProvider);
        _globalCacheConfig.setHiddenPackages(props.getHiddenPackages());
        _globalCacheConfig.setStatIntervalMinutes(props.getStatIntervalMinutes());
        _globalCacheConfig.setAreaInCacheName(props.isAreaInCacheName());
        _globalCacheConfig.setPenetrationProtect(props.isPenetrationProtect());
        _globalCacheConfig.setEnableMethodCache(props.isEnableMethodCache());
        _globalCacheConfig.setLocalCacheBuilders(autoConfigureBeans.getLocalCacheBuilders());
        _globalCacheConfig.setRemoteCacheBuilders(autoConfigureBeans.getRemoteCacheBuilders());
        return _globalCacheConfig;
    }
}
