/**
 * Created on 2018/6/7.
 */
package org.lemonframework.cache.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.core.ConfigurableObjectInputStream;

/**
 * spring decode.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class SpringJavaValueDecoder extends JavaValueDecoder {

    public static final SpringJavaValueDecoder INSTANCE = new SpringJavaValueDecoder(true);

    public SpringJavaValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    protected ObjectInputStream buildObjectInputStream(ByteArrayInputStream in) throws IOException {
        return new ConfigurableObjectInputStream(in, Thread.currentThread().getContextClassLoader());
    }
}
