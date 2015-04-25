package com.github.ebnew.ki4so.core.app;

import java.io.Serializable;

/**
 * 应用描述信息
 * @author zhenglu
 * @since 15/4/25
 */
public class KnightApp implements Serializable {

    private static final long serialVersionUID = -1850808070447330706L;


    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */

    private String appName;

    /***
     * 应用所在的主机地址
     */

    private String host;

    /**
     * 应用退出地址
     */

    private String logoutPath;


    /**
     * 是否是knight服务本身
     */
    private boolean knightService = false;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogoutPath() {
        return logoutPath;
    }

    public void setLogoutPath(String logoutPath) {
        this.logoutPath = logoutPath;
    }

    public boolean isKnightService() {
        return knightService;
    }

    public void setKnightService(boolean knightService) {
        this.knightService = knightService;
    }
}
