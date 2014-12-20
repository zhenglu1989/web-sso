package com.github.ebnew.ki4so.client;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;


/**
 * 用户和session的关联信息
 * @author burgess yang
 *
 */
public class UserRelationSession {
	
	/**
	 * 以用户名为key，session为value。在统一退出的时候获取并销毁session
	 */
	private static final Map<String, HttpSession> USERID_SESSION_MAP 
		= new HashMap<String, HttpSession>();
	

	/**
	 * 保存用户和session的关系
	 * @param userId 用户唯一标识
	 * @param session
	 */
	public  static void saveUserIdAndSessionId(String userId, 
			HttpSession session){
		USERID_SESSION_MAP.put(userId, session);
	}
	/**
	 * 保存用户和session的关系
	 * @param userId 用户唯一标识
	 * @param sessionId sessionID
	 */
	public static HttpSession  findSessionByUserId(String userId){
		return USERID_SESSION_MAP.get(userId);
	}
}
