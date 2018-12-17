package org.lemonframework.cache.test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.lemonframework.cache.Cache;
import org.lemonframework.cache.CacheResult;
import org.lemonframework.cache.CacheResultCode;
import org.lemonframework.cache.MultiLevelCache;
import org.lemonframework.cache.ProxyCache;

/**
 * base.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class AbstractCacheTest {

    protected Cache<Object, Object> cache;

    protected void baseTest() throws Exception {
        illegalArgTest();
    }

    private void illegalArgTest() {
        Assert.assertNull(cache.get(null));
        Assert.assertEquals(CacheResultCode.FAIL, cache.GET(null).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.GET(null).getMessage());

        Assert.assertNull(cache.getAll(null));
        Assert.assertEquals(CacheResultCode.FAIL, cache.GET_ALL(null).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.GET_ALL(null).getMessage());

        Assert.assertEquals(CacheResultCode.FAIL, cache.PUT(null, "V1").getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.PUT(null, "V1").getMessage());

        Assert.assertEquals(CacheResultCode.FAIL, cache.PUT(null, "V1", 1, TimeUnit.SECONDS).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.PUT(null, "V1", 1, TimeUnit.SECONDS).getMessage());

        Assert.assertEquals(CacheResultCode.FAIL, cache.PUT_ALL(null).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.PUT_ALL(null).getMessage());

        Assert.assertEquals(CacheResultCode.FAIL, cache.PUT_ALL(null, 1, TimeUnit.SECONDS).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.PUT_ALL(null, 1, TimeUnit.SECONDS).getMessage());

        try {
            Assert.assertFalse(cache.putIfAbsent(null, "V1"));
            Assert.assertEquals(CacheResultCode.FAIL, cache.PUT_IF_ABSENT(null, "V1", 1, TimeUnit.SECONDS).getResultCode());
            Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.PUT_IF_ABSENT(null, "V1", 1, TimeUnit.SECONDS).getMessage());
        } catch (UnsupportedOperationException e) {
            Cache c = cache;
            while (c instanceof ProxyCache) {
                c = ((ProxyCache) c).getTargetCache();
            }
            if (c instanceof MultiLevelCache) {
                // OK
            } else {
                Assert.fail();
            }
        }

        Assert.assertFalse(cache.remove(null));
        Assert.assertEquals(CacheResultCode.FAIL, cache.REMOVE(null).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.REMOVE(null).getMessage());

        Assert.assertEquals(CacheResultCode.FAIL, cache.REMOVE_ALL(null).getResultCode());
        Assert.assertEquals(CacheResult.MSG_ILLEGAL_ARGUMENT, cache.REMOVE_ALL(null).getMessage());

        try {
            cache.unwrap(String.class);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        Assert.assertNull(cache.tryLock(null, 1, TimeUnit.SECONDS));
        cache.tryLockAndRun(null, 1, TimeUnit.SECONDS, () -> Assert.fail());
    }
}
