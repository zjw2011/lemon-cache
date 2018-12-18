package org.lemonframework.cache.autoconfigure;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * bean dependency manager.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class BeanDependencyManager implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] autoInitBeanNames = beanFactory.getBeanNamesForType(AbstractCacheAutoInit.class, false, false);
        if (autoInitBeanNames != null) {
            BeanDefinition bd = beanFactory.getBeanDefinition(LemonCacheAutoConfiguration.GLOBAL_CACHE_CONFIG_NAME);
            String[] dependsOn = bd.getDependsOn();
            if (dependsOn == null) {
                dependsOn = new String[0];
            }
            int oldLen = dependsOn.length;
            dependsOn = Arrays.copyOf(dependsOn, dependsOn.length + autoInitBeanNames.length);
            System.arraycopy(autoInitBeanNames,0, dependsOn, oldLen, autoInitBeanNames.length);
            bd.setDependsOn(dependsOn);
        }
    }

}
