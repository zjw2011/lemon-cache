package org.lemonframework.cache.anno.support;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lemonframework.cache.CacheConfigException;
import org.lemonframework.cache.anno.KeyConvertor;
import org.lemonframework.cache.anno.SerialPolicy;
import org.lemonframework.cache.support.FastjsonKeyConvertor;
import org.lemonframework.cache.support.JavaValueDecoder;
import org.lemonframework.cache.support.JavaValueEncoder;
import org.lemonframework.cache.support.KryoValueDecoder;
import org.lemonframework.cache.support.KryoValueEncoder;
import org.lemonframework.cache.support.StatInfo;
import org.lemonframework.cache.support.StatInfoLogger;

/**
 * Created on 2016/11/29.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class ConfigProvider {

    protected static Map<String, String> parseQueryParameters(String query) {
        Map<String, String> m = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? pair.substring(0, idx) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
                if (key != null && value != null) {
                    m.put(key, value);
                }
            }
        }
        return m;
    }

    public Function<Object, byte[]> parseValueEncoder(String valueEncoder) {
        if (valueEncoder == null) {
            throw new CacheConfigException("no serialPolicy");
        }
        valueEncoder = valueEncoder.trim();
        URI uri = URI.create(valueEncoder);
        valueEncoder = uri.getPath();
        Map<String, String> params = parseQueryParameters(uri.getQuery());
        boolean useIdentityNumber = true;
        if ("false".equalsIgnoreCase(params.get("useIdentityNumber"))) {
            useIdentityNumber = false;
        }
        if (SerialPolicy.KRYO.equalsIgnoreCase(valueEncoder)) {
            return new KryoValueEncoder(useIdentityNumber);
        } else if (SerialPolicy.JAVA.equalsIgnoreCase(valueEncoder)) {
            return new JavaValueEncoder(useIdentityNumber);
        } else {
            throw new CacheConfigException("not supported:" + valueEncoder);
        }
    }

    public Function<byte[], Object> parseValueDecoder(String valueDecoder) {
        if (valueDecoder == null) {
            throw new CacheConfigException("no serialPolicy");
        }
        valueDecoder = valueDecoder.trim();
        URI uri = URI.create(valueDecoder);
        valueDecoder = uri.getPath();
        Map<String, String> params = parseQueryParameters(uri.getQuery());
        boolean useIdentityNumber = true;
        if ("false".equalsIgnoreCase(params.get("useIdentityNumber"))) {
            useIdentityNumber = false;
        }
        if (SerialPolicy.KRYO.equalsIgnoreCase(valueDecoder)) {
            return new KryoValueDecoder(useIdentityNumber);
        } else if (SerialPolicy.JAVA.equalsIgnoreCase(valueDecoder)) {
            return javaValueDecoder(useIdentityNumber);
        } else {
            throw new CacheConfigException("not supported:" + valueDecoder);
        }
    }

    JavaValueDecoder javaValueDecoder(boolean useIdentityNumber) {
        return new JavaValueDecoder(useIdentityNumber);
    }

    public Function<Object, Object> parseKeyConvertor(String convertor) {
        if (convertor == null) {
            return null;
        }
        if (KeyConvertor.FASTJSON.equalsIgnoreCase(convertor)) {
            return FastjsonKeyConvertor.INSTANCE;
        } else if (KeyConvertor.NONE.equalsIgnoreCase(convertor)) {
            return null;
        }
        throw new CacheConfigException("not supported:" + convertor);
    }

    public CacheNameGenerator createCacheNameGenerator(String[] hiddenPackages) {
        return new DefaultCacheNameGenerator(hiddenPackages);
    }

    public CacheContext newContext(GlobalCacheConfig globalCacheConfig) {
        return new CacheContext(globalCacheConfig);
    }

    public Consumer<StatInfo> statCallback() {
        return new StatInfoLogger(false);
    }
}
