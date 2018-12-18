package org.lemonframework.cache.anno.method;

/**
 * invoker.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface Invoker {
    Object invoke() throws Throwable;
}
