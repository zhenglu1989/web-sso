package com.github.ebnew.ki4so.core.authentication;

import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;

/**
 * 认证过的加密后的用户凭证，用于输出客户端
 * @author zhenglu
 * @since 15/4/27
 */
public class KnightEncryCredential extends KnightAbstractParameter implements KnightCredential{
    /**
     * 加密后的用户凭证串
     */
    private String credential;


    private KnightCredentialInfo credentialInfo;


    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public KnightCredentialInfo getCredentialInfo() {
        return credentialInfo;
    }

    public void setCredentialInfo(KnightCredentialInfo credentialInfo) {
        this.credentialInfo = credentialInfo;
    }

    @Override
    public boolean isOriginal() {
        return false;
    }
}
