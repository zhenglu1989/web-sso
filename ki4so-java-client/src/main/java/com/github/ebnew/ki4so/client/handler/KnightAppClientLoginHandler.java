package com.github.ebnew.ki4so.client.handler;

import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 本应用登录处理器接口，请实现该处理器
 * 实现本接口，将本应用的登录逻辑写在这里
 * @author zhenglu
 * @since 15/4/30
 */
public interface KnightAppClientLoginHandler {


    /**
     * 登录本应用
     * @param credentialInfo
     * @param request
     * @param response
     */
    public void loginClient(KnightCredentialInfo credentialInfo,HttpServletRequest request,HttpServletResponse response);
}
