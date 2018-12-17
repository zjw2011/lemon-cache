package org.lemonframework.cache.test.support;

import java.io.Serializable;

/**
 * query.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class DynamicQueryWithEquals implements Serializable {
    private static final long serialVersionUID = -8414259894084166465L;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DynamicQueryWithEquals) {
            return ((DynamicQueryWithEquals) obj).id == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new Long(id).hashCode();
    }
}
