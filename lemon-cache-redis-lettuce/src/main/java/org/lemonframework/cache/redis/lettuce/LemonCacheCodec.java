package org.lemonframework.cache.redis.lettuce;

import java.nio.ByteBuffer;

import io.lettuce.core.codec.RedisCodec;

/**
 * cache codec.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LemonCacheCodec implements RedisCodec {

    @Override
    public ByteBuffer encodeKey(Object key) {
        byte[] bytes = (byte[]) key;
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public Object decodeKey(ByteBuffer bytes) {
        return convert(bytes);
    }

    @Override
    public ByteBuffer encodeValue(Object value) {
        byte[] bytes = (byte[]) value;
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public Object decodeValue(ByteBuffer bytes) {
        return convert(bytes);
    }

    private Object convert(ByteBuffer bytes){
        byte[] bs = new byte[bytes.remaining()];
        bytes.get(bs);
        return bs;
    }


}
