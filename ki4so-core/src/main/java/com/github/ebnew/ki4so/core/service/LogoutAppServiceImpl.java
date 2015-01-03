package com.github.ebnew.ki4so.core.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.ebnew.ki4so.core.app.App;
import com.github.ebnew.ki4so.core.app.AppService;
import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatus;
import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatusStore;

public class LogoutAppServiceImpl implements LogoutAppService {

	private UserLoggedStatusStore userLoggedStatusStore ;

	private AppService appService;
	
	/**
	 * 登出app接口
	 */
	
	private LogoutAppService logoutAppService;
	
	public void setLogoutAppService(LogoutAppService logoutAppService) {
		this.logoutAppService = logoutAppService;
	}
	
	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public void setUserLoggedStatusStore(UserLoggedStatusStore userLoggedStatusStore) {
		this.userLoggedStatusStore = userLoggedStatusStore;
	}
	
	public LogoutAppServiceImpl(){
		
	}
	
	/**
	 * 该方法主要是退出app
	 */
	@Override
	public void logoutApp(String userId) {
		// 通过userID获取状态信息
		List<UserLoggedStatus> list = userLoggedStatusStore.findUserLoggedStatus(userId);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost;
		String url = "logoutByUserId.do";
		for (int i = 0; i < list.size(); i++) {
			UserLoggedStatus status = list.get(i);
			// 通过状态信息获取appID
			String appId = status.getAppId();
			// 通过appId获取app信息
			App app = appService.findAppById(appId);
			if (app == null) {
				continue;
			}
			// 调用让session失效的方法
			httpPost = new HttpPost(app.getHost() + url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("userId", userId));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					//这里是说明这个app登出成功了
					System.out.println("退出成功！！");
				}else {
					System.out.println( app.getAppId() + "退出失败！！");
				}
				//释放链接
				httpPost.releaseConnection();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
