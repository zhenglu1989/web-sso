package com.github.ebnew.ki4so.core.key;

import com.github.ebnew.ki4so.common.KnightDECoder;

import java.io.Serializable;
import java.security.Key;

/**
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightKey implements Serializable {

    private static final long serialVersionUID = 3535643339281384507L;
    /**
     * 私钥ID
     */
    private String keyId;


    /**
     * 应用id
     */
    private String appId;


    /**
     * 私钥值
     */
    private String value;

    /**
     * 私钥文件存放的路径
     */
    private String keyPath;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }


    public Key toSecurityKey(){
        try{
            if(this.getValue() != null){
                return KnightDECoder.initSecretKey(this.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;

    }
    @Override
    public String toString() {
        return "knight: [keyId=" + keyId + ", appId=" + appId + ", value="
                + value + ",keyPath=" + keyPath + "]";
    }
}
