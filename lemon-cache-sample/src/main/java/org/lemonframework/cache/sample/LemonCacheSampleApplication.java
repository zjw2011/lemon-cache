package org.lemonframework.cache.sample;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.lemonframework.cache.Cache;
import org.lemonframework.cache.CacheGetResult;
import org.lemonframework.cache.anno.CacheType;
import org.lemonframework.cache.anno.CreateCache;
import org.lemonframework.cache.anno.config.EnableCreateCacheAnnotation;
import org.lemonframework.cache.anno.config.EnableMethodCache;
import org.lemonframework.cache.sample.model.User;
import org.lemonframework.cache.sample.service.UserService;

/**
 * entry.
 * @author jiawei zhang
 */
@SpringBootApplication
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "org.lemonframework.cache.sample")
@RestController
public class LemonCacheSampleApplication {

    @CreateCache(expire = 100, cacheType = CacheType.LOCAL)
    private Cache<Long, String> userCache;

    @CreateCache(expire = 100, cacheType = CacheType.REMOTE)
    private Cache<String, User> userCaches;

    @CreateCache(expire = 100, cacheType = CacheType.REMOTE)
    private Cache<String, User> personCache;

    @Autowired
    private UserService userService;

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

    @GetMapping("/user")
    public User getUser() {
	    return userService.getUserById(1);
    }

    @GetMapping("/person")
    public List<User> getPerson() {

        personCache.put("3", new User(3, "jojo"));

	    Map<String, User> users = new HashMap<>();
        users.put("1", new User(1, "zhangsan"));
        users.put("2", new User(2, "wangwu"));

        userCaches.putAll(users);

        Set<String> keys = new LinkedHashSet<>();
        keys.add("1");
        keys.add("2");
        userCaches.GET_ALL(keys);

        return null;
    }

}

