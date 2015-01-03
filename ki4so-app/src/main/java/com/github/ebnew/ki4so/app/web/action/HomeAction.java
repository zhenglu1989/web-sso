package com.github.ebnew.ki4so.app.web.action;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.ebnew.ki4so.app.custom.Ki4soAppClientLoginHandlerImpl;
import com.github.ebnew.ki4so.client.UserRelationSession;
import com.github.ebnew.ki4so.client.web.filters.BaseClientFilter;
import com.github.ebnew.ki4so.web.utils.WebConstants;

@Controller
public class HomeAction {

	@RequestMapping("home")
	public ModelAndView home(HttpServletRequest request, HttpSession session){
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", session.getAttribute(Ki4soAppClientLoginHandlerImpl.USER_KEY));
		return mv;
	}
	@RequestMapping("loginOut")
	public void loginOut(HttpServletRequest request, HttpSession session,
			HttpServletResponse response){
		String userId = request.getParameter("userId");
		//保存用户和session的关系
		HttpSession sess = UserRelationSession.findSessionByUserId(userId);
		if(null!=sess){
			sess.setMaxInactiveInterval(0);
			UserRelationSession.remove(userId);
		}
		BaseClientFilter.getCookie(request, WebConstants.KI4SO_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
		String json = "{result:true}";
		//拼接jsonp格式的数据。
		StringBuffer sb = new StringBuffer();
		sb.append("(")
		.append(json)
		.append(");");
		//写入jsonp格式的数据。
		try {
			response.setContentType("application/x-javascript");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
}