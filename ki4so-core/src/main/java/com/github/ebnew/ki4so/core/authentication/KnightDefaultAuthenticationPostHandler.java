package com.github.ebnew.ki4so.core.authentication;

import org.apache.log4j.Logger;

/**
 * 默认的后置处理器，提供抽象的方法由具体的子类实现
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightDefaultAuthenticationPostHandler implements KnightAuthenticationPostHandler{

    private static final Logger logger = Logger.getLogger(KnightDefaultAuthenticationPostHandler.class);
    /**
     *   服务端过期时间设置为3个月
     */

    private static final Long SERVER_INVALID = 3L*30*24*60*60*1000;

    /**
     * 客户端失效时间为1分钟
     */
    private static final Long CLIENT_INVALID = 60L * 1000;
    @Override
    public KnightAuthentication postAuthentication(KnightCredential credential, KnightUser user) {
        return null;
    }
}
