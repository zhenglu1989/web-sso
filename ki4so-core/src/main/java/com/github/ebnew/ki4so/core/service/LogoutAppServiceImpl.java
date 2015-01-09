package com.github.ebnew.ki4so.core.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.util.IOUtils;
import com.github.ebnew.ki4so.core.app.App;
import com.github.ebnew.ki4so.core.app.AppService;
import com.github.ebnew.ki4so.core.authentication.status.UserLoggedStatus;
//	/**
//	 * 登出app接口
//	 */
//	private LogoutAppService logoutAppService;
//	public void setLogoutAppService(LogoutAppService logoutAppService) {
//		this.logoutAppService = logoutAppService;
//	}
/**
	 * 该方法主要是退出app
	 */
// 通过userID获取状态信息
// 通过状态信息获取appID
// 通过appId获取app信息
// 调用让session失效的方法
//这里是说明这个app登出成功了
public class LogoutAppServiceImpl implements LogoutAppService {
	private static Logger logger = Logger.getLogger(LogoutAppServiceImpl.class);
	private AppService appService;
	
	/**
	 * 用户登录状态集合，不允许重复状态值。
	 */
	private Set<UserLoggedStatus> loggedStatus = new HashSet<UserLoggedStatus>();
	
	public Set<UserLoggedStatus> getLoggedStatus() {
		return loggedStatus;
	}
	/**
	 * 用户标识和用户状态列表之间的映射表，相当于一个索引，方便根据用户标识查询所有的登录状态标。
	 * 其中map中的key是用户标识，value是该用户所有的登录状态列表。 
	 */
	private Map<String, List<UserLoggedStatus>> userIdIndexMap = new HashMap<String, List<UserLoggedStatus>>();
	
	public Map<String, List<UserLoggedStatus>> getUserIdIndexMap() {
		return userIdIndexMap;
	}
	
//	/**
//	 * 登出app接口
//	 */
//	private LogoutAppService logoutAppService;
	
	public LogoutAppServiceImpl(){}
	
//	public void setLogoutAppService(LogoutAppService logoutAppService) {
//		this.logoutAppService = logoutAppService;
//	}
	
	public void setAppService(AppService appService) {
		this.appService = appService;
	}
	Map<String, Integer> urlmap=new HashMap<String, Integer>();
	/**
	 * 该方法主要是退出app
	 */
	@Override
	public void logoutApp(final String userId) {
		// 通过userID获取状态信息
<<<<<<< HEAD
//		List<UserLoggedStatus> list = findUserLoggedStatus(userId);
		
		String url = "loginOut.do";
		RequestConfig requestConfig = RequestConfig.custom()  
                .setSocketTimeout(3000).setConnectTimeout(3000).build();  
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()  
                .setDefaultRequestConfig(requestConfig).build(); 
        try {
			httpclient.start();  
			final HttpPost[] httpPosts=new HttpPost[5];
			for (int i = 0; i < 5; i++) {
//				UserLoggedStatus status = list.get(i);
//				 String appId = status.getAppId();
//				 App app = appService.findAppById(appId);
//				 if(app != null){
				if(i==2){
					 HttpPost httpPost=new HttpPost("http://h12312ao.360.cn/");
					 List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					 nvps.add(new BasicNameValuePair("userId", userId));
					 try {
						httpPost.setEntity(new UrlEncodedFormEntity(nvps));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 httpPosts[i]=httpPost;
				}else{
					HttpPost httpPost=new HttpPost("http://hao.360.cn/");
					 List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					 nvps.add(new BasicNameValuePair("userId", userId));
					 try {
						httpPost.setEntity(new UrlEncodedFormEntity(nvps));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 httpPosts[i]=httpPost;
				}
//				 }
				
			}
			
			final CountDownLatch latch = new CountDownLatch(httpPosts.length);
			for(int i=0;i<httpPosts.length;i++){
				final  HttpPost httpPost=httpPosts[i];
				httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {  
			        //无论完成还是失败都调用countDown()  
			        @Override  
			        public void completed(final HttpResponse response) {  
			            if(response.getStatusLine().getStatusCode()==200){
			            	System.out.println("退出成功userid=>"+userId+"httpPost"+httpPost.getURI());
			            	logger.info("退出成功userid=>"+userId+"httpPost"+httpPost.getURI());
			            	latch.countDown();
			            }
			        }  
			        public void failed(final Exception ex) { 
			        	latch.countDown();
			        	System.out.println("退出失败userid=>"+userId+"httpPost"+httpPost.getURI());
			        	logger.error("退出失败userid=>"+userId+"httpPost"+httpPost.getURI());
			        }  
			        @Override  
			        public void cancelled() {  
			        	latch.countDown();  
			        }  
			    });
=======
		List<UserLoggedStatus> list = userLoggedStatusStore.findUserLoggedStatus(userId);
		HttpPost httpPost;
		String url = "logout.do";
		for (int i = 0; i < list.size(); i++) {
			UserLoggedStatus status = list.get(i);
			// 通过状态信息获取appID
			String appId = status.getAppId();
			// 通过appId获取app信息
			App app = appService.findAppById(appId);
			if (app == null) {
				continue;
>>>>>>> 41a86918dce05348c27164b7059f49766d916167
			}
			//latch.await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		for (int i = 0; i < list.size(); i++) {
//			UserLoggedStatus status = list.get(i);
//			// 通过状态信息获取appID
//			String appId = status.getAppId();
//			// 通过appId获取app信息
//			App app = appService.findAppById(appId);
//			if (app == null) {
//				continue;
//			}
//			// 调用让session失效的方法
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			httpPost = new HttpPost(app.getHost() + url);
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("userId", userId));
//			try {
//				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//				HttpResponse response = httpClient.execute(httpPost);
//				if (response.getStatusLine().getStatusCode() == 200) {
//					//这里是说明这个app登出成功了
//					System.out.println("退出成功！！");
//				}else {
//					System.out.println( app.getAppId() + "退出失败！！");
//				} 
//				
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}finally {
//				httpClient.close();
//			}
//		}

	}

	

	@Override
	public synchronized void addUserLoggedStatus(UserLoggedStatus userLoggedStatus) {
		//检查数据的合法性。
		if(userLoggedStatus!=null 
				&& !StringUtils.isEmpty(userLoggedStatus.getAppId())
				&& !StringUtils.isEmpty(userLoggedStatus.getUserId())
				){
			//避免数据为空。
			if(userLoggedStatus.getLoggedDate()==null){
				userLoggedStatus.setLoggedDate(new Date());
			}
			this.loggedStatus.add(userLoggedStatus);
			List<UserLoggedStatus> list = this.userIdIndexMap.get(userLoggedStatus.getUserId());
			if(list==null){
				list = new ArrayList<UserLoggedStatus>();
				this.userIdIndexMap.put(userLoggedStatus.getUserId(), list);
			}
			list.add(userLoggedStatus);
		}
	}

	@Override
	public synchronized void deleteUserLoggedStatus(String userId, String appId) {
		//检查数据的合法性。
		if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(appId)){
			UserLoggedStatus status = new UserLoggedStatus(userId, appId);
			this.loggedStatus.remove(status);
			List<UserLoggedStatus> list = this.userIdIndexMap.get(userId);
			if(list!=null){
				list.remove(status);
			}
		}
	}
	
	@Override
	public synchronized void clearUpUserLoggedStatus(String userId) {
		if(!StringUtils.isEmpty(userId)){
			List<UserLoggedStatus> list = this.userIdIndexMap.get(userId);
			if(list!=null){

				logoutApp(userId);
				list.clear();
				this.userIdIndexMap.put(userId, null);

			}
		}
	}


	@Override
	public List<UserLoggedStatus> findUserLoggedStatus(String userId) {
		if(!StringUtils.isEmpty(userId)){
			return this.userIdIndexMap.get(userId);
		}
		return null;
	}
	public static void main(String[] args) {
		LogoutAppServiceImpl lo=new LogoutAppServiceImpl();
		lo.logoutApp("");
		System.out.println("===============");
	}
}