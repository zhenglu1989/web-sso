package com.github.ebnew.ki4so.core.service;

public interface LogoutAppService {
	
	/**
	 * 登出用户ID为userId的用户，登出该用户已经登录过的所有用户。
	 * 而service值对应的应用应该优先登出。
	 * @param userId 用户ID。
	 * @param service 用户优先登出的URL地址。
	 */
	public void logoutApp(final String userId, final String service);

}
