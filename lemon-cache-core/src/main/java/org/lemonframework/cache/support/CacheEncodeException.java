package org.lemonframework.cache.support;

import org.lemonframework.cache.CacheException;

/**
 * exception.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheEncodeException extends CacheException {

    private static final long serialVersionUID = -1768444197009616269L;

    public CacheEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
