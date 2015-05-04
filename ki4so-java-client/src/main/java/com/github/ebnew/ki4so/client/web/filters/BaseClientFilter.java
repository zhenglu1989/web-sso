package com.github.ebnew.ki4so.client.web.filters;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.github.ebnew.ki4so.common.utils.StringUtils;

import java.io.IOException;

/**
 * 公共基础的客户端过滤器类，定义了一些公共的方法。
 * @author zhenglu
 *
 */
public abstract class BaseClientFilter implements Filter{

    //服务器主机地址
    protected String knightServerHost = "http://localhost:8080/ki4so-web";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       knightServerHost = getInitParamterWithDefaultValue(filterConfig,"knightServerHost",knightServerHost);
        doInit(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    /**
     * 由子类实现的初始化方法
     * @param filterConfig
     * @throws ServletException
     */
    protected abstract void doInit(FilterConfig filterConfig) throws ServletException;

    /**
     * 获取过滤器的参数值，带有默认值，如果没有配置，则试用默认值
     * @param filterConfig
     * @param paramName
     * @param defaultValue
     * @return
     */
    protected String getInitParamterWithDefaultValue(FilterConfig filterConfig,String paramName,String defaultValue){
       String value = filterConfig.getInitParameter(paramName);
        if(StringUtils.isEmpty(value)){
            value = defaultValue;
        }
        return value;
    }
    public static Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }
}
