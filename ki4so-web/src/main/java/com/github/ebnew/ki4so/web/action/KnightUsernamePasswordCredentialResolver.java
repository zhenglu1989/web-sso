package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.authentication.KnightNamePasswordCredential;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户名和密码凭据解析器，从参数中解析出用户名和用户密码
 * @author zhenglu
 * @since 15/4/29
 */
public class KnightUsernamePasswordCredentialResolver extends KnightAbstractParameterCredentialResolver {
    /**
     * 用户名的参数名
     */
    public static final String USERNAME_PARAM_NAME = "username";

    /**
     * 密码的参数名
     */
    public static final String PASSWORD_PARAM_NAME = "password";

    @Override
    protected KnightCredential doResolveCredential(HttpServletRequest request) {
        String username = request.getParameter(USERNAME_PARAM_NAME);
        String password = request.getParameter(PASSWORD_PARAM_NAME);
        if(request != null && !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            KnightNamePasswordCredential credential = new KnightNamePasswordCredential();
            credential.setUsername(username);
            credential.setPassword(password);
            return credential;

        }
        return null;
    }
}
