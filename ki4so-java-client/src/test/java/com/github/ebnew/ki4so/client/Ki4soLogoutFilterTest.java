package com.github.ebnew.ki4so.client;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.ebnew.ki4so.client.web.filters.Ki4soLogoutFilter;


public class Ki4soLogoutFilterTest {
	
	private Logger logger = Logger.getLogger(Ki4soLogoutFilterTest.class);
	
	private Ki4soLogoutFilter ki4soLogoutFilter;
	
	@Before
	public void beforeKi4soLogoutFilterTest(){
		ki4soLogoutFilter = new Ki4soLogoutFilter();
	}
	
	@Test
	public void buildRedirectToKi4soServerTest(){
		//测试输入Null的情况。
		Assert.assertEquals("http://localhost:8080/ki4so-web/logout.do?service=http://localhost:8080/ki4so-app", 
				ki4soLogoutFilter.buildRedirectToKi4soServer());
		
	}
	
	@After
	public void afterKi4soLogoutFilterTest(){
		ki4soLogoutFilter = null;
	}

}
