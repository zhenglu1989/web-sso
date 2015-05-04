package com.github.ebnew.ki4so.app.custom;

import com.github.ebnew.ki4so.client.handler.KnightAppClientLogoutHandler;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhenglu
 * @since 15/4/30
 */
public class AppClientLogoutHandlerImpl implements KnightAppClientLogoutHandler {

    private static final Logger logger = Logger.getLogger(AppClientLogoutHandlerImpl.class);



    @Override
    public void logoutClient(HttpServletRequest request, HttpServletResponse response, String userId) {
       if(!StringUtils.isEmpty(userId)){
           //remove the exception

           logger.info("the user id is userid has logout the app");
       }
    }
}
