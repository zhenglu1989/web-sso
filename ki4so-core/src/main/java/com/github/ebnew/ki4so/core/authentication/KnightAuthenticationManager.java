package com.github.ebnew.ki4so.core.authentication;

/**
 * 认证管理器，负责对用户凭证进行有效性
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightAuthenticationManager {

    /**
     * fixedby zhenglu 失败可以返回null，不必抛出异常。保证系统可用性
     * 对用户凭证进行认证，若失败则抛出异常，若成功则放回认证结果
     * @return
     */
    public KnightAuthentication authentication(KnightCredential credential);
}
