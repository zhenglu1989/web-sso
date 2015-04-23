package com.github.ebnew.ki4so.core.authentication;

/**
 * 用户凭据，代表了一个用户的身份，这是一个抽象的概念
 * 这种凭据可以是一个用户名和密码对，也可以是一个加密后的信息，也可以是人和可以识别用户的信息
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightCredential {


    /**
     * 是否是原始凭据，即未认证过的原始信息
     *
     * @return true ：原始凭据，false：加密后的凭据
     */
    public boolean isOriginal();
}
