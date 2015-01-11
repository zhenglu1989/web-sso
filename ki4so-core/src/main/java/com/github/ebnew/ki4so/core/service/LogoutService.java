package com.github.ebnew.ki4so.core.service;


/**
 * 统一登出接口，该接口主要负责服务端统一登出sso登录的所有业务应用。
 * 
 * @author ywbrj042
 */
public interface LogoutService {
	
	/**
	 * 统一退出所有登录的业务应用接口。
	 * @param userId 用户ID.
	 * @param servcie 登出之后要跳转的URL地址，要同步登出该URL对应的应用。
	 */
	public void logout(String userId, String servcie);

}
