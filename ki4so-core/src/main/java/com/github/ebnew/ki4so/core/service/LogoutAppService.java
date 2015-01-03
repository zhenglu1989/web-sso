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
	

}
