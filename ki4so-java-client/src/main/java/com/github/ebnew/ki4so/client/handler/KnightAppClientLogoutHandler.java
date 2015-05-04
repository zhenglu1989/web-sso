package com.github.ebnew.ki4so.client.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 本应用登出处理接口
 * @author zhenglu
 * @since 15/4/30
 */
public interface KnightAppClientLogoutHandler {
    /**
     * 退出本应用
     * @param request
     * @param response
     * @param userId
     */

    public void logoutClient(HttpServletRequest request,HttpServletResponse response,String userId);
}
