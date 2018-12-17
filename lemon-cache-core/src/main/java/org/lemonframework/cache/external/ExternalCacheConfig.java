package org.lemonframework.cache.external;

import java.util.function.Function;

import org.lemonframework.cache.CacheConfig;
import org.lemonframework.cache.support.DecoderMap;
import org.lemonframework.cache.support.JavaValueEncoder;

/**
 * config.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class ExternalCacheConfig<K, V> extends CacheConfig<K, V> {
    private String keyPrefix;
    private Function<Object, byte[]> valueEncoder = JavaValueEncoder.INSTANCE;
    private Function<byte[], Object> valueDecoder = DecoderMap.defaultJavaValueDecoder();

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Function<Object, byte[]> getValueEncoder() {
        return valueEncoder;
    }

    public void setValueEncoder(Function<Object, byte[]> valueEncoder) {
        this.valueEncoder = valueEncoder;
    }

    public Function<byte[], Object> getValueDecoder() {
        return valueDecoder;
    }

    public void setValueDecoder(Function<byte[], Object> valueDecoder) {
        this.valueDecoder = valueDecoder;
    }
}
