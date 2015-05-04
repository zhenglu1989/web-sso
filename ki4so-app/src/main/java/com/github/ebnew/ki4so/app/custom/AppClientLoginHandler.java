package com.github.ebnew.ki4so.app.custom;

import com.github.ebnew.ki4so.client.handler.KnightAppClientLoginHandler;
import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  默认的登录认证实现
 * @author zhenglu
 * @since 15/4/30
 */
public class AppClientLoginHandler implements KnightAppClientLoginHandler{

   private static Logger logger = Logger.getLogger(AppClientLoginHandler.class);

    public static final String  USER_KEY = "USER_KEY_SESSION";

    @Override
    public void loginClient(KnightCredentialInfo credentialInfo, HttpServletRequest request, HttpServletResponse response) {
     request.getSession().setAttribute(USER_KEY,credentialInfo);
       logger.info("the user id is  "+ credentialInfo.getUserId() + "has logined in the app");
    }
}
