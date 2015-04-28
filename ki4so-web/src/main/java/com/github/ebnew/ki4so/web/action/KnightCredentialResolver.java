package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;

import javax.servlet.http.HttpServletRequest;

/**
 * 凭据解析，从http请求的cookie,参数等值中解析出各种类型的用户凭证，该接口由具体实现类具体解析凭据
 * @author zhenglu
 * @since 15/4/28
 */
public interface KnightCredentialResolver {

    /**
     *  从http请求参数的cookie或参数值中解析出凭据信息对象，返回解析后的凭据对象
     * @param request
     * @return
     */

    public KnightCredential resolveCredential(HttpServletRequest request);

}
