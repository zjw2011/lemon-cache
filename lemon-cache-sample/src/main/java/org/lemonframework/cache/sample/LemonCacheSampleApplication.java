package org.lemonframework.cache.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.lemonframework.cache.Cache;
import org.lemonframework.cache.CacheGetResult;
import org.lemonframework.cache.anno.CacheType;
import org.lemonframework.cache.anno.CreateCache;
import org.lemonframework.cache.anno.config.EnableCreateCacheAnnotation;

@SpringBootApplication
@EnableCreateCacheAnnotation
@RestController
public class LemonCacheSampleApplication {

    @CreateCache(expire = 100, cacheType = CacheType.LOCAL)
    private Cache<Long, String> userCache;

	public static void main(String[] args) {
		SpringApplication.run(LemonCacheSampleApplication.class, args);
	}

    @GetMapping("/hello")
    public void hello() {
        final String user2 = userCache.get(123L);
        System.out.println(user2);
        final CacheGetResult<String> user = userCache.GET(123L);
        System.out.println(user);
//        userCache.put(123L, user);
        //userCache.remove(123L);
        //final CacheResult zhangsan = userCache.PUT(123L, "zhangsan");
        //System.out.println(zhangsan);
//        final CacheGetResult<String> r = userCache.GET(123L);
//        System.out.println(r.getValue());
//        System.out.println(r.getMessage());
    }

}

