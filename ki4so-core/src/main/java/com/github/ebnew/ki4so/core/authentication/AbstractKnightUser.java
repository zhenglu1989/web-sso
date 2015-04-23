package com.github.ebnew.ki4so.core.authentication;

import java.util.Map;

/**
 * 抽象的用户主体实现类
 * @author zhenglu
 * @since 15/4/23
 */
public abstract  class AbstractKnightUser implements KnightUser {
    /**
     * 用户主体的唯一标识
     */
    protected String id;
    /**
     * 用户主体的其他属性
     */
    protected Map<String,Object> attributes;

    public AbstractKnightUser(){
        super();
    }


    public AbstractKnightUser(String id, Map<String, Object> attributes) {
        super();
        this.id = id;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
