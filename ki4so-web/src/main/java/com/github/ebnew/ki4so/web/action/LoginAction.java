package com.github.ebnew.ki4so.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.service.KnightService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.ebnew.ki4so.core.service.LoginResult;
import com.github.ebnew.ki4so.web.utils.WebConstants;

/**
 * 登入web控制器类，处理登录的请求。
 * @author 不二
 *
 */
@Controller
public class LoginAction {
	
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class);
	
	@Autowired
	protected KnightCredentialResolver credentialResolver;
	
	@Autowired
	protected KnightService ki4soService;
	
	@Autowired
	protected LoginResultToView loginResultToView;



	/**
	 * 登录接口，该接口处理所有与登录有关的请求。
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
		LOGGER.debug("enter login action");
		//解析用户凭据。
        KnightCredential credential = credentialResolver.resolveCredential(request);
		//没有提供任何认证凭据。
		if(credential==null){
			//设置serivce地址到session中。
			String service = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
			LOGGER.debug("the servcie is "+service);
			if(!StringUtils.isEmpty(service)){
				request.getSession().setAttribute(WebConstants.KI4SO_SERVICE_KEY_IN_SESSION, service);
			}
			LOGGER.info("no credential, return login page");
			//返回到登录页面，索取用户凭据。
			return mv;
		}
		//提供了用户凭据
		else{
			//调用核心结果进行凭据认证。
			LoginResult result = ki4soService.login(credential);
			//将验证结果转换为视图输出结果。
			mv = loginResultToView.loginResultToView(mv, result, request, response);
		}
		return mv;
	}
	

}
