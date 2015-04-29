package com.github.ebnew.ki4so.core.service;

import java.io.Serializable;

import com.github.ebnew.ki4so.core.authentication.KnightAuthentication;

/**
 * 登录结果对象。
 * @author burgess yang
 *
 */
public class LoginResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2497393519640031346L;
	
	/**
	 * 认证是否成功，
	 */
	private boolean success;

	/**
	 * 认证失败的错误代码。
	 */
	private String code;
	
	
	/**
	 * 认证失败的错误提示信息键值。
	 */
	private String msgKey;
	
	/**
	 * 认证结果信息对象。
	 */
	private KnightAuthentication authentication;


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMsgKey() {
		return msgKey;
	}


	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

    public KnightAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(KnightAuthentication authentication) {
        this.authentication = authentication;
    }
}
