package org.lemonframework.cache.sample.model;

import java.io.Serializable;

/**
 * user.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7945042260453871892L;
    private Integer id;

    private String name;

    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
