package org.lemonframework.cache;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.lemonframework.cache.embedded.CaffeineCacheBuilder;

/**
 * 测试LinkedHashMapCache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CaffeineCacheTest {

    @Test
    public void testBuilder() {
        Cache<String, Integer> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .buildCache();
        for (int i = 0; i < 102; i++) {
            System.out.println("s:i:" + i);
            cache.put("i" + i, i);
            System.out.println("e:i:" + i);
            Integer val = cache.get("i" + i);
            Assertions.assertThat(val).isEqualTo(i);
        }
        Integer val = cache.get("i0");
        Assertions.assertThat(val).isEqualTo(0);
        val = cache.get("i1");
        Assertions.assertThat(val).isEqualTo(1);
        val = cache.get("i2");
        Assertions.assertThat(val).isEqualTo(2);
        val = cache.get("i101");
        Assertions.assertThat(val).isEqualTo(101);
    }

}
