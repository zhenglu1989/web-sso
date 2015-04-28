package com.github.ebnew.ki4so.core.authentication.status;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录状态
 * @author zhenglu
 * @since 15/4/28
 */
public class KnightUserLoginStatus implements Serializable{


    private static final long serialVersionUID = 8453108828607661563L;
    /**
     * 登录用户的标识
     */
    private String userId;

    /**
     * 用户登录的应用标识
     */

    private String appId;

    /**
     * 登录应用的时间
     */

    private Date loginDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}
