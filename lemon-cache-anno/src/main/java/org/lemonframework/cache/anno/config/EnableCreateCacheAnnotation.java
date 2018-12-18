package org.lemonframework.cache.anno.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import org.lemonframework.cache.anno.field.CreateCacheAnnotationBeanPostProcessor;

/**
 * enable create cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CreateCacheAnnotationBeanPostProcessor.class})
public @interface EnableCreateCacheAnnotation {
}