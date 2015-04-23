package com.github.ebnew.ki4so.core.authentication;

import java.util.Date;
import java.util.Map;

/**
 * 验证结果
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightAuthentication {

    public Map<String,Object> getAttrbutes();

    public Date getAuthenticateDate();

    public KnightUser getUser();


}
