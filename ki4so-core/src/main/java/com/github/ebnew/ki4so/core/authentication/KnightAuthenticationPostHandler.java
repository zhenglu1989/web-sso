package com.github.ebnew.ki4so.core.authentication;

/**
 * 认证成功后的处理器，该接口的职责是将用户认证主体，用户凭据转换为一个合适的认证结果对象
 * 根据用户凭据中的信息和参数进行合适的转换
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightAuthenticationPostHandler {


    public static final String KNIGHT_SERVER_EC_KEY = "knight_ser_ec_key";

    public static final String KNIGHT_CLIENT_EC_KEY = "knight_client_ec_key";
}
