package com.github.ebnew.ki4so.core.service;

import java.util.List;

import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatus;



/**
 * 该接口主要定义了一个退出app的方法
 */
public interface LogoutAppService {
	
	/**
	 * 该方法主要是退出app
	 */
	public void logoutApp(String userId);
	
	
	/**
	 * 增加新的用户登录状态。
	 * @param userLoggedStatus 用户登录状态。
	 */
	public void addUserLoggedStatus(UserLoggedStatus userLoggedStatus);
	
	/**
	 * 删除用户登录状态。
	 * @param userId 用户标识。
	 * @param appId 应用标识。
	 */
	public void deleteUserLoggedStatus(String userId, String appId);
	
	/**
	 * 清除某个用户所有的登录状态。
	 * @param userId 用户标识。
	 */
	public void clearUpUserLoggedStatus(String userId);
	
	/**
	 * 查询用户标识对用的用户所有的登录状态。
	 * @param userId 用户标识。
	 * @param 用户登录状态。
	 */
	public List<UserLoggedStatus> findUserLoggedStatus(String userId);
	

}
