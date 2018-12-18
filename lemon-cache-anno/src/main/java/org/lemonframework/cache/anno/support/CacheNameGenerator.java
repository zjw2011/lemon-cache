package org.lemonframework.cache.anno.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * cache name generator.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface CacheNameGenerator {

    String generateCacheName(Method method, Object targetObject);

    String generateCacheName(Field field);
}
