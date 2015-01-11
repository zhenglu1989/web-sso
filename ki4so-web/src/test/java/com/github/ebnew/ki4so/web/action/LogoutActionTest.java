package com.github.ebnew.ki4so.web.action;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.github.ebnew.ki4so.core.service.Ki4soService;
import com.github.ebnew.ki4so.web.utils.WebConstants;

/**
 * 登出测试类。
 * @author burgess yang
 *
 */
public class LogoutActionTest {
	
	@Autowired
	private LogoutAction logoutAction;

	@Before
	public void setUp() throws Exception {
		logoutAction = new LogoutAction();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testLogoutWithoutCredential() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		HttpSession session = request.getSession();
		CredentialResolver credentialResolver = Mockito.mock(CredentialResolver.class);
		logoutAction.setCredentialResolver(credentialResolver);
		
		Ki4soService ki4soService = Mockito.mock(Ki4soService.class);
		logoutAction.setKi4soService(ki4soService);
		
		//测试没有cookie的情况。即要登出的凭据不存在的情况。则返回默认的登出成功页面。
		ModelAndView mv = logoutAction.logout(request, response, session);
		Assert.assertEquals(0, response.getCookies().length);
		Assert.assertEquals("logoutSucess", mv.getViewName());
	}
	
	@Test
	public void testLogoutWithCredentialButNoService() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		HttpSession session = request.getSession();
		CredentialResolver credentialResolver = Mockito.mock(CredentialResolver.class);
		logoutAction.setCredentialResolver(credentialResolver);
		Ki4soService ki4soService = Mockito.mock(Ki4soService.class);
		logoutAction.setKi4soService(ki4soService);
		
		//测试存在cookie，登出后要清除cookie值，但是service参数的值是null的情况。
		request.setCookies(new Cookie(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY, "dddsd"));
		ModelAndView mv = logoutAction.logout(request, response,session);
		Assert.assertEquals(1, response.getCookies().length);
		Assert.assertEquals(0, response.getCookies()[0].getMaxAge());
		Assert.assertEquals("logoutSucess", mv.getViewName());
	}
	
	
	@Test
	public void testLogoutWithCredentialAndService() throws IOException {
		String servce  = "http://app.com/logoutSucess.do";
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(WebConstants.SERVICE_PARAM_NAME, servce);
		MockHttpServletResponse response = new MockHttpServletResponse();
		HttpSession session = request.getSession();
		CredentialResolver credentialResolver = Mockito.mock(CredentialResolver.class);
		logoutAction.setCredentialResolver(credentialResolver);
		Ki4soService ki4soService = Mockito.mock(Ki4soService.class);
		logoutAction.setKi4soService(ki4soService);
		
		//测试存在cookie，登出后要清除cookie值，但是service参数的值是null的情况。
		request.setCookies(new Cookie(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY, "dddsd"));
		
		ModelAndView mv = logoutAction.logout(request, response,session);
		Assert.assertEquals(1, response.getCookies().length);
		Assert.assertEquals(0, response.getCookies()[0].getMaxAge());
		RedirectView view = (RedirectView) mv.getView();
		Assert.assertEquals(servce, view.getUrl());
	}

}
