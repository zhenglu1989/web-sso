package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供凭据解析前和后处理方法的抽象解析器类
 * @author zhenglu
 * @since 15/4/29
 */
public abstract class KnightAbstractPreAndPostProcessingCredentialResolver implements KnightCredentialResolver{

    @Override
    public KnightCredential resolveCredential(HttpServletRequest request) {
        this.preResolveCredential(request);
        KnightCredential credential = this.doResolveCredential(request);

        return this.postResolveCredential(request,credential);
    }

    /**
     * 凭据解析之前的处理
     * @param request 请求参数对象
     *
     */
    protected void preResolveCredential(HttpServletRequest request){
    }


    /**
     *  抽象方法，实现真正的凭据解析处理
     * @param request  请求参数对象
     * @return 解析后的凭据对象信息
     */
    protected abstract KnightCredential doResolveCredential(HttpServletRequest request);

    /**
     * 凭据解析后处理
     * @param request 请求参数对象
     * @param credential 解析后的凭据信息，要基于该凭据上增加属性值
     * @return 处理后的凭据解析器
     */
    protected KnightCredential postResolveCredential(HttpServletRequest request,KnightCredential credential){
        return credential;
    }

}
