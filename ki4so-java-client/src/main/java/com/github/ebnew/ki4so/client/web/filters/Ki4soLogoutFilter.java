package com.github.ebnew.ki4so.client.web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ki4so处理登出 请求的过滤器。
 * @author burgess yang
 *
 */
public class Ki4soLogoutFilter extends BaseClientFilter {
	
	//当前应用的登出地址
	private String currentAppLogoutUrl = "http://localhost:8080/ki4so-web/logout.do";
	
	//登出成功跳转地址
	private String logoutSuccessUrl = "http://localhost:8080/ki4so-app";
	
	@Override
	public void doInit(FilterConfig filterConfig) throws ServletException {
		//初始化参数值。
		currentAppLogoutUrl = this.getInitParameterWithDefalutValue(filterConfig, "currentAppLogoutUrl", currentAppLogoutUrl);
		logoutSuccessUrl = this.getInitParameterWithDefalutValue(filterConfig, "logoutSuccessUrl", logoutSuccessUrl);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		//若已经登录，则作相关处理。
		servletResponse.sendRedirect(buildRedirectToKi4soServer(servletRequest));
		return;
	}
	
	protected String buildRedirectToKi4soServer(HttpServletRequest servletRequest){
		StringBuffer sb = new StringBuffer(this.currentAppLogoutUrl);
		if(this.currentAppLogoutUrl.contains("?")){
			sb.append("&");
		}
		else{
			sb.append("?");
		}
		sb.append("service=").append(logoutSuccessUrl);
		return sb.toString();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}

