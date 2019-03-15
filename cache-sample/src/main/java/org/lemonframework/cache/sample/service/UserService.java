package org.lemonframework.cache.sample.service;

import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.lemonframework.cache.sample.model.User;

/**
 * user service.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Service
public class UserService {

    @Cached(name="userCache.", key="#userId", expire = 3600)
    public User getUserById(int userId) {
        User user = new User();
        user.setId(userId);
        user.setName("liso");
        return user;
    }

    @CacheUpdate(name="userCache.", key="#user.userId", value="#user")
    public void updateUser(User user) {

    }

    @CacheInvalidate(name="userCache.", key="#userId")
    public void deleteUser(int userId) {

    }
}
