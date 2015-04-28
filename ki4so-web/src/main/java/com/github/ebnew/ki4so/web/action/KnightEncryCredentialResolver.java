package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.authentication.KnightEncryCredential;
import com.github.ebnew.ki4so.web.utils.WebConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 经过认证加密后的凭据信息解析器，从http请求的cookie中解析出对应的加密后的凭据信息
 * @author zhenglu
 * @since 15/4/28
 */
public class KnightEncryCredentialResolver implements KnightCredentialResolver{

    private static final Logger logger = Logger.getLogger(KnightEncryCredentialResolver.class);

    @Override
    public KnightCredential resolveCredential(HttpServletRequest request) {
        if(request != null){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                String value = null;
                for(Cookie cookie:cookies){
                   if(cookie != null && cookie.getName().equalsIgnoreCase(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY));
                     value = cookie.getValue();
                     break;
                }
                //如果cookie中没有凭据值，则从请求参数中获取凭据值
                if(StringUtils.isEmpty(value)){
                   logger.info("KI4SO_SERVER_EC value is empty" );
                    value = request.getParameter(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
                }
                //最终如果加密凭据有值，则直接返回凭据对象
                if(!StringUtils.isEmpty(value)){
                     return new KnightEncryCredential();
                }
            }
        }

        return null;
    }
}
