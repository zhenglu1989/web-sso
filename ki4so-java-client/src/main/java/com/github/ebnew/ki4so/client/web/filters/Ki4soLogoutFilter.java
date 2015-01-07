package com.github.ebnew.ki4so.client.web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.github.ebnew.ki4so.client.handler.AppClientLogoutHandler;
import com.github.ebnew.ki4so.common.utils.StringUtils;

/**
 * ki4so处理登出 请求的过滤器。
 * @author burgess yang
 *
 */
public class Ki4soLogoutFilter extends BaseClientFilter {
	
	//当前应用的登出地址
	private static String currentAppLogoutUrl = "http://localhost:8080/ki4so-web/logout.do";
	
	//登出成功跳转地址
	private static String logoutSuccessUrl = "http://localhost:8080/ki4so-app";
	
	/**
	 * 登录本应用处理器类，由此类进行构造一个对象。
	 */
	protected String appClientLogoutHandlerClass = "com.github.ebnew.ki4so.app.custom.Ki4soAppClientLogoutHandlerImpl";

	Logger logger = Logger.getLogger(Ki4soLogoutFilter.class);
	/**
	 * 登录本应用的处理器。
	 */
	protected AppClientLogoutHandler appClientLogoutHandler;
	
	@Override
	public void doInit(FilterConfig filterConfig) throws ServletException {
		logger.debug("doInit begin ..");
		//初始化参数值。
		currentAppLogoutUrl = this.getInitParameterWithDefalutValue(filterConfig, "currentAppLogoutUrl", currentAppLogoutUrl);
		logoutSuccessUrl = this.getInitParameterWithDefalutValue(filterConfig, "logoutSuccessUrl", logoutSuccessUrl);
		appClientLogoutHandlerClass = getInitParameterWithDefalutValue(filterConfig, "appClientLoginHandlerClass", appClientLogoutHandlerClass);
		//构造登录本应用的处理器对象。
		if(!StringUtils.isEmpty(appClientLogoutHandlerClass)){
			try{
				this.appClientLogoutHandler = (AppClientLogoutHandler)Class.forName(appClientLogoutHandlerClass).newInstance();
			}catch (Exception e) {
				logger.error("Class for appClientLogoutHandler is error ",e);
			}
		}
		logger.debug("doInit end ..");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.debug(" doFilter begin .." );
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		HttpServletRequest servletRequest = (HttpServletRequest)request;
//		HttpSession session = servletRequest.getSession();
		if(appClientLogoutHandler!=null){
			//登出本应用。
			appClientLogoutHandler.logoutClient(servletRequest, servletResponse);
		}
		//若已经登录，则作相关处理。
		servletResponse.sendRedirect(buildRedirectToKi4soServer());
		logger.debug(" doFilter end .." );
		return;
	}
	
	public String buildRedirectToKi4soServer(){
		StringBuffer sb = new StringBuffer(currentAppLogoutUrl);
		if(currentAppLogoutUrl.contains("?")){
			sb.append("&");
		}
		else{
			sb.append("?");
		}
		sb.append("service=").append(logoutSuccessUrl);
		logger.debug("logout and logoutSuccessUrl:"+sb.toString());
		return sb.toString();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}

