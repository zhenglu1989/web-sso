package com.github.ebnew.ki4so.client.web.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;

import com.github.ebnew.ki4so.client.key.DefaultKeyServiceImpl;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.key.KeyService;

/**
 * @author Administrator
 *
 */
public class Ki4soGeneratePrivateKeyFileFilter extends BaseClientFilter{

	private static Logger logger = Logger.getLogger(DefaultKeyServiceImpl.class
			.getName());
	private String ki4soServerFetchKeyUrl = null;
	//运用标识
	private String appId = null;
	/**
	 * 生成私钥文件类
	 */
	protected String ki4soGeneratePrivateKeyFileClass = "com.github.ebnew.ki4so.client.key.DefaultKeyServiceImpl";
	
	protected KeyService keyService= null; 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			keyService.generateKeyFile(appId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "秘钥生成失败");
		}
		//过滤器继续执行
		filter.doFilter(request, response);
		
	}

	@Override
	protected void doInit(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		ki4soGeneratePrivateKeyFileClass = getInitParameterWithDefalutValue(filterConfig, "appClientDefaultKeyServiceClass", ki4soGeneratePrivateKeyFileClass);
		//获取appId参数值
		appId = getInitParameterWithDefalutValue(filterConfig,"ki4soClientAppId","1001");
		//获取服务器访问路径参数值
		ki4soServerFetchKeyUrl = getInitParameterWithDefalutValue(filterConfig,"ki4soServerFetchKeyUrl","http://localhost:8080/ki4so-web/fetchKey.do");
		//构造登录本应用的处理器对象。
		if(!StringUtils.isEmpty(ki4soGeneratePrivateKeyFileClass)){
			try{
				//实例化
				this.keyService = (KeyService) (Class.forName(ki4soGeneratePrivateKeyFileClass)
											.getConstructor(String.class,String.class)).newInstance(ki4soServerFetchKeyUrl,appId);	//实现类需无参构造方法
			}catch (Exception e) {
				// TODO: handle exception
				logger.log(Level.SEVERE, e.getMessage());		//记录初始化类日志
			}
		}
	}
}
