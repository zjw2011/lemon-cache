package org.lemonframework.cache;

/**
 * lock.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface AutoReleaseLock extends AutoCloseable {
    /**
     * Release the lock use Java 7 try-with-resources.
     */
    @Override
    void close();
}
