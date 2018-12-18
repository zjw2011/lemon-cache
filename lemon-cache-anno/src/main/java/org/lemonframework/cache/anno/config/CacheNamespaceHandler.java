package org.lemonframework.cache.anno.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * handle cache namesapce.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("annotation-driven", new CacheAnnotationParser());
    }
}
