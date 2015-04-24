package com.github.ebnew.ki4so.core.key;

/**
 * 利用非对称加密保证key的安全性
 * @author zhenglu
 * @since 15/4/24
 */
public interface KnightKeyService {

    /**
     * 根据密钥id查找对应的密钥信息
     * @param keyId
     * @return
     */
    public KnightKey findKeyByKeyId(String keyId);

    /**
     * 根据应用id查找对应的密钥信息
     * @param appId
     * @return
     */
    public KnightKey findKeyByAppId(String appId);


    /**
     * 判断密钥文件是否生成
     * @param token 改文件存在的标识
     * @return
     */
    public boolean checkKeyFileExistByToken(String token);


    /**
     * 生成非对称加密文件(客户端生成私钥文件；服务端生成公钥文件)
     * @param token
     * @return
     */
    public Object generateKeyFile(String token);

}
