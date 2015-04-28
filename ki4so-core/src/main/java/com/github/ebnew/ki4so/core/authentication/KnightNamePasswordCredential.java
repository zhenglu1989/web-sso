package com.github.ebnew.ki4so.core.authentication;

/**
 * 用户名和密码形式的未经过认证的原始用户凭证
 * @author zhenglu
 * @since 15/4/27
 */
public class KnightNamePasswordCredential extends KnigthAbstractParameter implements KnightCredential{
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isOriginal() {
        return false;
    }
}
