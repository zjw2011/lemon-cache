package org.lemonframework.cache;

/**
 * exception.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheInvokeException extends CacheException {

    private static final long serialVersionUID = -9002505061387176702L;

    public CacheInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheInvokeException(Throwable cause) {
        super(cause);
    }

}