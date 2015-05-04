package com.github.ebnew.ki4so.client.web.filters;

import com.github.ebnew.ki4so.app.custom.AppClientLoginHandler;
import com.github.ebnew.ki4so.client.handler.KnightAppClientLoginHandler;
import com.github.ebnew.ki4so.client.key.DefaultKeyServiceImpl;
import com.github.ebnew.ki4so.client.session.SessionStorage;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.authentication.KnightEncryCredential;
import com.github.ebnew.ki4so.core.authentication.KnightEncryCredentialManagerImpl;
import com.github.ebnew.ki4so.core.key.KnightKey;
import com.github.ebnew.ki4so.core.key.KnightKeyService;
import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;
import com.github.ebnew.ki4so.web.utils.WebConstants;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * knight客户端应用的过滤器，从而实现集成knight单点登录系统
 * @author zhenglu
 * @since 15/4/30
 */
public class KnightClientFilter extends BaseClientFilter {

    private static Logger logger = Logger.getLogger(KnightClientFilter.class);

    //在客户端的session中的用户信息，避免频繁认证，提高性能
    public static final String USER_STATE_IN_SESSION_KEY = "knight_client_user_info_session_key";

    //将服务器登出地址设置到request属性中
    public static final String KNIGHT_SERVER_LOGOUT_URL = "knight_server_logout_url";


    // 登录服务器url地址
    protected String knightServerLoginUrl = knightServerHost +"login.do";

      //获取应用密钥信息的url地址
    protected String knightServerFetchKeyUrl = knightServerHost + "fetchKey.do";

    //退出服务器url地址

    protected String knightServerLogoutUrl = knightServerHost + "logout.do";

    //本应用服务器上的应用id
    protected String knightClientAppId = "1001";

   //本应用对应的加密key
    protected KnightKey knightKey;

   //密钥获取服务类
    protected KnightKeyService keyService;

    //凭据管理器

    protected KnightEncryCredentialManagerImpl encryCredentialManager;

    //登录本应用的处理器

    protected KnightAppClientLoginHandler appClientLoginHandler;

    /**
     * 登录本应用处理器类，由此类进行构造一个对象。
     */
    protected String appClientLoginHandlerClass = "com.github.ebnew.ki4so.app.custom.AppClientLogoutHandlerImpl";



    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
        knightClientAppId = getInitParamterWithDefaultValue(filterConfig, "knightClientAppId", knightClientAppId);
        knightServerLoginUrl = getInitParamterWithDefaultValue(filterConfig, "ki4soServerLoginUrl", knightServerLoginUrl);
        knightServerLogoutUrl = getInitParamterWithDefaultValue(filterConfig, "ki4soServerLogoutUrl", knightServerLogoutUrl);
        knightServerFetchKeyUrl = getInitParamterWithDefaultValue(filterConfig, "ki4soServerFetchKeyUrl", knightServerFetchKeyUrl);
        appClientLoginHandlerClass = getInitParamterWithDefaultValue(filterConfig, "appClientLoginHandlerClass", appClientLoginHandlerClass);
        //构造key服务等相关对象。
        //构造登录本应用的处理器对象。
        if(!StringUtils.isEmpty(appClientLoginHandlerClass)){
            try{
                this.appClientLoginHandler = (AppClientLoginHandler)Class.forName(appClientLoginHandlerClass).newInstance();
            }catch (Exception e) {
                // TODO: handle exception
            }
        }
        keyService = new DefaultKeyServiceImpl(knightServerFetchKeyUrl, knightClientAppId);
        this.encryCredentialManager = new KnightEncryCredentialManagerImpl();
        this.encryCredentialManager.setKeyService(keyService);
        logger.info("the ki4so sever is :"+this.knightServerHost+", please check this service is ok.");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession();
        request.setAttribute(KNIGHT_SERVER_LOGOUT_URL,knightServerLogoutUrl);
        //本地应用未登录
        try{
            String knight_client_ec = getClientEC(request);

            if(StringUtils.isEmpty(knight_client_ec)){
                //跳转到knight登录页面
                response.sendRedirect(buildRedirectToKnightServer(request));
                return;
            }
            if(knightKey == null){
                KnightKey key =  keyService.findKeyByAppId(knightClientAppId);
            }
            //new认证过的加密后的用户凭证
            KnightEncryCredential encryCredential  =   new KnightEncryCredential();
            encryCredential.setCredential(knight_client_ec);
            KnightCredentialInfo knightCredentialInfo = this.encryCredentialManager.decrypt(encryCredential);
            if(knightCredentialInfo != null){
                //检查凭据的合法性
                boolean valid = this.encryCredentialManager.checkEncryCredentialInfo(knightCredentialInfo);
                if(valid){
                    session.setAttribute(USER_STATE_IN_SESSION_KEY,knightCredentialInfo);
                    if(appClientLoginHandler != null){
                        //登录本应用
                        appClientLoginHandler.loginClient(knightCredentialInfo,request,response);
                    }
                    //重新定位到原请求，去除ec参数
                    String url = request.getRequestURL().toString();
                    if(!StringUtils.isEmpty(url)){
                        //如果请求中存在ec参数，则去除这个参数，重定位
                        if(url.contains(WebConstants.KI4SO_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY)){
                            url = url.substring(0,url.indexOf(WebConstants.KI4SO_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY));
                            if(url.contains("?")){
                                url = url.substring(0,url.length() - 1);
                            }
                            // 去掉末尾的问好
                            if(url.endsWith("&")){
                                url = url.substring(0,url.length() - 1);
                            }
                        }

                    }
                    SessionStorage.put(knightCredentialInfo.getUserId(),session);
                    response.sendRedirect(url);
                    return;
                }
            }
            filterChain.doFilter(request,response);
            return;

        }catch (Exception e){
            logger.error("filter error：： " + e.getMessage());
            response.sendRedirect(buildRedirectToKnightServer(request));
            return;

        }
    }


    @Override
    public void destroy() {


    }

   protected String buildRedirectToKnightServer(HttpServletRequest request){
       StringBuffer buffer = new StringBuffer(this.knightServerLoginUrl);
       if(this.knightServerLoginUrl.contains("?")){
          buffer.append("&");
       }else {
           buffer.append("?");
       }
       buffer.append("service=").append(request.getRequestURL().toString());

    return buffer.toString();
   }

    /**
     * 从http中获得cookie认证加密后的凭据
     * @param request
     * @return
     */
    protected String getClientEC(HttpServletRequest request){
        String ec = null;
        if(request!=null){
            ec = request.getParameter(WebConstants.KI4SO_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
        }
        return ec;

    }
}
