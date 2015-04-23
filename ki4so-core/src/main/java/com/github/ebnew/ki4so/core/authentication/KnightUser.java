package com.github.ebnew.ki4so.core.authentication;

import java.util.Map;
/**
 * 用户主体，代表一个用户
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightUser {

    public Map<String,Object> getAttributes();

    public String getId();
}
