package com.github.ebnew.ki4so.core.authentication;


import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;

/**
 * 加密凭据的管理器，包括对加密凭据加密和解密等操作
 * @author zhenglu
 * @since 15/4/27
 */
public interface KnightEncryCredentialManager {


    /**
     * 对斑马的凭据信息进行解码，解码后为一个凭据对象
     * @param credential
     * @return
     */
    public KnightCredentialInfo decrypt(KnightEncryCredential credential);

    /**
     * 对一个凭据对象进行加密，返回加密后的字符串
     * @param credentialInfo
     * @return
     */

    public String encrypt(KnightCredentialInfo credentialInfo);

    /**
     * 检查用户凭据信息的合法性，是否合法，是否过期 是否有效等
     * @param credentialInfo
     * @return
     */

    public boolean checkEncryCredentialInfo(KnightCredentialInfo credentialInfo);


}
