package org.lemonframework.cache;

/**
 * exception.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheConfigException extends CacheException {
    private static final long serialVersionUID = -3401839239922905427L;

    public CacheConfigException(Throwable cause) {
        super(cause);
    }

    public CacheConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheConfigException(String message) {
        super(message);
    }
}
