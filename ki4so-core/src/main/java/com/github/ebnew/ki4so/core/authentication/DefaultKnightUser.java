package com.github.ebnew.ki4so.core.authentication;


import java.util.Map;

/**
 * 默认的用户主体对象
 * @author zhenglu
 * @since 15/4/23
 */
public class DefaultKnightUser extends AbstractKnightUser {

    public DefaultKnightUser() {
        super();
    }

    public DefaultKnightUser(String id, Map<String, Object> attributes) {
        super(id, attributes);
    }
}
