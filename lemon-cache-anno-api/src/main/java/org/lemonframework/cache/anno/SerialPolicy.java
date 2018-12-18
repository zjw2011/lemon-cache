package org.lemonframework.cache.anno;

import java.util.function.Function;

/**
 * serial policy.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface SerialPolicy {
    String JAVA = "JAVA";

    String KRYO = "KRYO";

    Function<Object, byte[]> encoder();

    Function<byte[], Object> decoder();
}
