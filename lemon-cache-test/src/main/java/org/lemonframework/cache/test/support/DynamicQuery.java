package org.lemonframework.cache.test.support;

import java.io.Serializable;

/**
 * query.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class DynamicQuery implements Serializable {
    private static final long serialVersionUID = 2076557820895585156L;

    private long id;
    private String name;
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
