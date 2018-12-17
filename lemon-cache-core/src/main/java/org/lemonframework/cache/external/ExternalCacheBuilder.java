package org.lemonframework.cache.external;

import java.util.function.Function;

import org.lemonframework.cache.AbstractCacheBuilder;

/**
 * builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class ExternalCacheBuilder<T extends ExternalCacheBuilder<T>> extends AbstractCacheBuilder<T> {

    @Override
    public ExternalCacheConfig getConfig() {
        if (config == null) {
            config = new ExternalCacheConfig();
        }
        return (ExternalCacheConfig) config;
    }

    public T keyPrefix(String keyPrefix){
        getConfig().setKeyPrefix(keyPrefix);
        return self();
    }

    public T valueEncoder(Function<Object, byte[]> valueEncoder){
        getConfig().setValueEncoder(valueEncoder);
        return self();
    }

    public T valueDecoder(Function<byte[], Object> valueDecoder){
        getConfig().setValueDecoder(valueDecoder);
        return self();
    }

    public void setKeyPrefix(String keyPrefix){
        getConfig().setKeyPrefix(keyPrefix);
    }

    public void setValueEncoder(Function<Object, byte[]> valueEncoder){
        getConfig().setValueEncoder(valueEncoder);
    }

    public void setValueDecoder(Function<byte[], Object> valueDecoder){
        getConfig().setValueDecoder(valueDecoder);
    }
}
