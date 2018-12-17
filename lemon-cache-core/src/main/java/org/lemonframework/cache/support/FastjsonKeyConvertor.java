package org.lemonframework.cache.support;

import java.util.function.Function;

import com.alibaba.fastjson.JSON;

/**
 * key converter.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class FastjsonKeyConvertor implements Function<Object, Object> {

    public static final FastjsonKeyConvertor INSTANCE = new FastjsonKeyConvertor();

    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        if (originalKey instanceof String) {
            return originalKey;
        }
        return JSON.toJSONString(originalKey);
    }

}

