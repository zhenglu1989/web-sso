package com.github.ebnew.ki4so.client.web.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.github.ebnew.ki4so.client.session.SessionStorage;
import com.github.ebnew.ki4so.common.utils.StringUtils;

/**
 * ki4so客户端应用登出的过滤器。
 * 处理客户端应用本身的登出逻辑。
 * @author Administrator
 *
 */
public class Ki4soClientLogoutFilter extends BaseClientFilter{
	
	private static  Logger logger = Logger.getLogger(Ki4soClientLogoutFilter.class.getName());

	private static final String SESSIONID_IS_NULL="send sessionId is null";
	
	private static final String SESSIONID_IS_NOT_CONTATINS ="send sessionId is not be included for Client ";
	
	@Override
	public void doInit(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		String sessionId = request.getParameter("userId");
		if(StringUtils.isEmpty(sessionId)){
			logger.warn(SESSIONID_IS_NULL);
			sendError(servletResponse,SESSIONID_IS_NULL);
			return;
		}
		if(!SessionStorage.containsKey(sessionId)){
			logger.warn(SESSIONID_IS_NOT_CONTATINS);
			sendError(servletResponse,SESSIONID_IS_NOT_CONTATINS);
			return;
		}
		HttpSession session = SessionStorage.get(sessionId);
		try{
			//本地应用已经登录，则进行登出处理。
			if(session.getAttribute(Ki4soClientFilter.USER_STATE_IN_SESSION_KEY)!=null){
				//清除session中的值。
				session.setAttribute(Ki4soClientFilter.USER_STATE_IN_SESSION_KEY, null);
				//将session设置过期
				session.setMaxInactiveInterval(0);
				//移除session信息
				SessionStorage.remove(sessionId);
			}
			//响应登录结果。
			sendResponse(servletResponse);
		}
		catch (Exception e) {
			//响应登录结果。
			sendError(servletResponse);
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private void sendResponse(HttpServletResponse response){
		response.setContentType("text/javascript;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter outhtml;
		try {
			outhtml = response.getWriter();
			outhtml.print("{result:true}");
			outhtml.close();
		} catch (IOException e) {
			logger.error("send sendResponse error", e);
		} 
	}
	
	private void sendError(HttpServletResponse response){
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			logger.error("send response error", e);
		} 
	}
	
	private void sendError(HttpServletResponse response,String msg){
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
		} catch (IOException e) {
			logger.error("send response error", e);
		} 
	}
	
	

}
