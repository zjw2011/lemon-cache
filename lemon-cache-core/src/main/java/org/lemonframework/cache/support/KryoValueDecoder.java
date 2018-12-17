package org.lemonframework.cache.support;

import java.io.ByteArrayInputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

/**
 * decode.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class KryoValueDecoder extends AbstractValueDecoder {

    public static final KryoValueDecoder INSTANCE = new KryoValueDecoder(true);

    public KryoValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    public Object doApply(byte[] buffer) {
        ByteArrayInputStream in;
        if (useIdentityNumber) {
            in = new ByteArrayInputStream(buffer, 4, buffer.length - 4);
        } else {
            in = new ByteArrayInputStream(buffer);
        }
        Input input = new Input(in);
        Kryo kryo = (Kryo) KryoValueEncoder.kryoThreadLocal.get()[0];
        kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
        return kryo.readClassAndObject(input);
    }
}
