package com.github.ebnew.ki4so.app.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeAction {

    public static final String  USER_KEY = "USER_KEY_SESSION";

	@RequestMapping("home")
	public ModelAndView home(HttpServletRequest request, HttpSession session){
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", session.getAttribute(USER_KEY));
		return mv;
	}
}