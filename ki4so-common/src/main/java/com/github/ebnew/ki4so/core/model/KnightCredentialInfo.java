package com.github.ebnew.ki4so.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 加密凭证信息
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightCredentialInfo implements Serializable{

    private static final long serialVersionUID = -6546063621536994328L;
    /**
     * 应用唯一标识
     */
    private String appId;

    /**
     * 用户唯一标识
     */

    private String userId;

    /**
     * 密钥唯一标识
     */
    private String keyId;


    /**
     * 加密凭证的创建时间
     */
    private Date createTime;


    /**
     * 加密凭证的失效时间
     */
    private Date expireTime;

    /**
     * 加密凭证的盐值
     */

    private String salt;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
