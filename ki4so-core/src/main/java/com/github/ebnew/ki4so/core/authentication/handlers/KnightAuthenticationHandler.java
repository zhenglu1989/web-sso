package com.github.ebnew.ki4so.core.authentication.handlers;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;

/**
 * 认证处理类，该类处理检查用户的凭证是否合法
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightAuthenticationHandler {
    /**
     * 认证方法，返回true表示认证成功，false表示认证失败
     * @param credential
     * @return
     */

    public boolean authenticate(KnightCredential credential);


    /**
     * 是否支持用户凭证的认证处理，true表示支持
     * @param credential
     * @return
     */
    public boolean supports(KnightCredential credential);
}
