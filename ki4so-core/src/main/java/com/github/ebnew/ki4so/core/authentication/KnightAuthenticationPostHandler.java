package com.github.ebnew.ki4so.core.authentication;

/**
 * 认证成功后的处理器，该接口的职责是将用户认证主体，用户凭据转换为一个合适的认证结果对象
 * 根据用户凭据中的信息和参数进行合适的转换
 * @author zhenglu
 * @since 15/4/23
 */
public interface KnightAuthenticationPostHandler {


    /**
     * 服务本身的加密凭据信息存储在验证结果对象 服务端attrbutes动态属性key
     */
    public static final String KNIGHT_SERVER_EC_KEY = "knight_ser_ec_key";

    /**
     * 服务本身的加密凭据信息存储在验证结果对象 客户端attrbutes动态属性key
     */
    public static final String KNIGHT_CLIENT_EC_KEY = "knight_client_ec_key";

    /**
     *  认证后的处理方法，将用户的凭据和主体转换为一个认证的结果对象
     * @param credential
     * @param user
     * @return
     */

    public KnightAuthentication postAuthentication(KnightCredential credential,KnightUser user);


}
