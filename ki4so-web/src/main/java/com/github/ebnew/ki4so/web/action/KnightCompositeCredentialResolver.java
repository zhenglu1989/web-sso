package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.core.authentication.KnightAbstractParameter;
import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.web.utils.WebConstants;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 组合凭据解析器，组合两种解析器，按照优先级顺序，从http请求参数或者cookie中解析出优先级比较高的凭据，若无优先级高的凭据，则按照顺序解析
 * @author zhenglu
 * @since 15/4/29
 */
public class KnightCompositeCredentialResolver implements KnightCredentialResolver{

    //加密后的凭据解析器
    private KnightCredentialResolver encryCredentialResolver;

    private KnightCredentialResolver usernamePasswordCredentialResolver;


    @Override
    public KnightCredential resolveCredential(HttpServletRequest request) {
        if(request == null){
            return  null;
        }
        KnightCredential credential = null;
        if(encryCredentialResolver != null){
            //先解析加密后的凭证
            credential = encryCredentialResolver.resolveCredential(request);
        }
        //若返回为空，则用原始凭据解析
        if(credential == null){
           credential  = usernamePasswordCredentialResolver.resolveCredential(request);
        }
        if(credential instanceof KnightAbstractParameter){
            KnightAbstractParameter parameter = (KnightAbstractParameter)credential;
            parameter.setParameters(WebUtils.getParametersStartingWith(request, null));
            if(parameter.getParameterValue(WebConstants.SERVICE_PARAM_NAME) == null){
                if(request.getSession().getAttribute(WebConstants.KI4SO_SERVICE_KEY_IN_SESSION) != null){
                    parameter.getParameters().put(WebConstants.SERVICE_PARAM_NAME,request.getSession().getAttribute(WebConstants.KI4SO_SERVICE_KEY_IN_SESSION));

                }
            }
        }
        return credential;
    }

    public void setEncryCredentialResolver(KnightCredentialResolver encryCredentialResolver) {
        this.encryCredentialResolver = encryCredentialResolver;
    }

    public void setUsernamePasswordCredentialResolver(KnightCredentialResolver usernamePasswordCredentialResolver) {
        this.usernamePasswordCredentialResolver = usernamePasswordCredentialResolver;
    }
}
