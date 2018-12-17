package org.lemonframework.cache;

/**
 * exception.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = -9066209768410752667L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
