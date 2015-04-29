package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.authentication.KnightParameter;

import javax.servlet.http.HttpServletRequest;

/**
 * 该类提供了参数化的凭据类型的解析后处理方法，将请求中的所有参数全部转移到参数列表中，供相关处理
 * @author zhenglu
 * @since 15/4/29
 */
public abstract class KnightAbstractParameterCredentialResolver extends  KnightAbstractPreAndPostProcessingCredentialResolver {
    @Override
    protected KnightCredential postResolveCredential(HttpServletRequest request, KnightCredential credential) {
        if(credential == null){
            return null;
        }
        if(credential instanceof KnightParameter){
            KnightParameter  parameter = (KnightParameter)credential;
            parameter.setParameters(request.getParameterMap());
        }

        return super.postResolveCredential(request, credential);
    }
}
