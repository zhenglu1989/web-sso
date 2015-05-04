package com.github.ebnew.ki4so.client.web.filters;

import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.key.KnightKeyService;
import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * 生成密钥的filter
 * @author zhenglu
 * @since 15/5/4
 */
public class KnightGeneratePrivateKeyFilter extends BaseClientFilter {

    private static final Logger logger = Logger.getLogger(KnightGeneratePrivateKeyFilter.class);

    private String serverFetchKeyUrl = null;

    //应用标识
    private String appId = null;

    //生成密钥文件类
    protected String GeneratePrivateKeyFileClass = "com.github.ebnew.ki4so.client.key.DefaultKeyServiceImpl";

    protected KnightKeyService keyService;

    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {

        // TODO Auto-generated method stub
        GeneratePrivateKeyFileClass = getInitParamterWithDefaultValue(filterConfig, "appClientDefaultKeyServiceClass", GeneratePrivateKeyFileClass);
        //获取appId参数值
        appId = getInitParamterWithDefaultValue(filterConfig, "appId", "1001");
        //获取服务器访问路径参数值
        serverFetchKeyUrl = getInitParamterWithDefaultValue(filterConfig, "serverFetchKeyUrl", "http://localhost:8080/ki4so-web/fetchKey.do");
        //构造登录本应用的处理器对象。
        if(!StringUtils.isEmpty(GeneratePrivateKeyFileClass)){
            try{
                //实例化
                this.keyService = (KnightKeyService) (Class.forName(GeneratePrivateKeyFileClass)
                        .getConstructor(String.class,String.class)).newInstance(serverFetchKeyUrl,appId);	//实现类需无参构造方法
            }catch (Exception e) {
                // TODO: handle exception
                logger.error("init failure::" + e.getMessage());
            }
        }
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{

            keyService.generateKeyFile(appId);

        }catch (Exception e){
            logger.error("密钥生成失败");
        }
        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
}
