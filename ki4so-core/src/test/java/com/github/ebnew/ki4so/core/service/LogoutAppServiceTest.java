package com.github.ebnew.ki4so.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.ebnew.ki4so.core.app.App;
import com.github.ebnew.ki4so.core.app.AppService;
import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatus;
import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatusStore;

public class LogoutAppServiceTest {
	
	private LogoutAppServiceImpl logoutAppService;
	
	private AppService appService;
	
	private UserLoggedStatusStore userLoggedStatusStore;
	
	@Before
	public void setUp(){
		logoutAppService = new LogoutAppServiceImpl();
		appService = Mockito.mock(AppService.class);
		logoutAppService.setAppService(appService);
		userLoggedStatusStore = Mockito.mock(UserLoggedStatusStore.class);
		logoutAppService.setUserLoggedStatusStore(userLoggedStatusStore);
	}
	
	@Test
	public void testLogoutAppWithEmptyUserId(){
		
		logoutAppService.logoutApp(null, null);
		
		logoutAppService.logoutApp("", null);
		
	}
	
	@Test
	public void testLogoutAppWithUserIdAndServiceJustLoginedServiceApp(){
		
		String userId = "test";
		String url = "http://www.yuecai.com/html/index/index.html";
		
		App serivceApp = new App();
		serivceApp.setLogoutUrl(url);
		
		Mockito.when(appService.findAppByHost(url)).thenReturn(serivceApp);
		Mockito.when(userLoggedStatusStore.findUserLoggedStatus(userId)).thenReturn(null);
		
		logoutAppService.logoutApp(userId, url);
		
	}
	
	@Test
	public void testLogoutAppWithUserIdAndServiceHasLoginedTwoApps(){
		
		String userId = "test";
		String url = "http://git.oschina.net/ywbrj042/ki4so";
		
		App serivceApp = new App();
		serivceApp.setLogoutUrl(url);
		serivceApp.setAppId("1");
		
		App firstApp = new App();
		firstApp.setLogoutUrl(url);
		firstApp.setAppId("1001");
		
		App sencondApp = new App();
		sencondApp.setLogoutUrl(url);
		sencondApp.setAppId("1002");
		
		List<UserLoggedStatus> list = new ArrayList<UserLoggedStatus>();
		UserLoggedStatus userLoggedStatus = new UserLoggedStatus(userId, "1001");
		list.add(userLoggedStatus);
		userLoggedStatus = new UserLoggedStatus(userId, "1002");
		list.add(userLoggedStatus);
		userLoggedStatus = new UserLoggedStatus(userId, "1");
		list.add(userLoggedStatus);
		
		Mockito.when(appService.findAppByHost(url)).thenReturn(serivceApp);
		
		Mockito.when(appService.findAppById("1001")).thenReturn(serivceApp);
		Mockito.when(appService.findAppById("1001")).thenReturn(firstApp);
		Mockito.when(appService.findAppById("1002")).thenReturn(sencondApp);
		Mockito.when(userLoggedStatusStore.findUserLoggedStatus(userId)).thenReturn(list);
		
		logoutAppService.logoutApp(userId, url);
		
	}



}
