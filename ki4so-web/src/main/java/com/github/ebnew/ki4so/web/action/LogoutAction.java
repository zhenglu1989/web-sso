package com.github.ebnew.ki4so.web.action;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.service.KnightService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.github.ebnew.ki4so.client.web.filters.Ki4soClientFilter;
import com.github.ebnew.ki4so.core.authentication.Credential;
import com.github.ebnew.ki4so.core.service.Ki4soService;
import com.github.ebnew.ki4so.web.utils.WebConstants;

/**
 * 登出web控制器，处理登出的请求。
 * @author burgess yang
 *
 */
@Controller
public class LogoutAction {

	private static final Logger LOGGER = Logger.getLogger(LogoutAction.class);
	
	@Autowired
	protected KnightCredentialResolver credentialResolver;
	
	@Autowired
	protected KnightService ki4soService;
	
	public void setKi4soService(KnightService ki4soService) {
		this.ki4soService = ki4soService;
	}

	/**
	 * 设置用户凭据解析器。
	 * @param credentialResolver
	 */
	public void setCredentialResolver(KnightCredentialResolver credentialResolver) {
		this.credentialResolver = credentialResolver;
	}
	
	/**
	 * 处理登出ki4so服务器的请求。
	 * 1.清除用户登录的状态信息，即用户登录了那些应用。
	 * 2.清除sso服务端的cookie。
	 * 3.统一登出用户登出过的所有应用。
	 * @param request 请求对象。
	 * @param response 响应对象。
	 * 如果参数servcie有合法的值，则跳转到该地址。否则返回到默认的登出成功页面。
	 * @throws IOException 
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws IOException {
		
		ModelAndView modelAndView = new ModelAndView();
		
		//获得service.
		String service = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
		LOGGER.info("the service of logout is "+service);
		//解析用户凭据。
		KnightCredential credential = credentialResolver.resolveCredential(request);
		//调用servie统一登出所有的应用。
		this.ki4soService.logout(credential, service);
		
		//清除cookie值。
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0){
			for(Cookie cookie:cookies){
				if(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY.equals(cookie.getName())){
					//设置过期时间为立即。
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					LOGGER.info("clear up the cookie "+WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
				}
			}
		}
		
		if(!StringUtils.isEmpty(service)){
			//跳转到service对应的URL地址
			modelAndView.setView(new RedirectView(service));
			session.setAttribute(Ki4soClientFilter.USER_STATE_IN_SESSION_KEY,null);
		}
		else{
			//返回默认的登出成功页面。
			modelAndView.setViewName("logoutSucess");
		}
		return modelAndView;
	}

}
