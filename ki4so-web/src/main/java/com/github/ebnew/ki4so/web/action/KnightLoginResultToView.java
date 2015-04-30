package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.core.service.LoginResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该接口实现了将登录结果转换为视图响应的功能
 * @author zhenglu
 * @since 15/4/30
 */
public interface KnightLoginResultToView {

    /**
     *
     * @param mav
     * @param result
     * @param request
     * @param response
     * @return
     */
    public ModelAndView loginResultToView(ModelAndView mav,LoginResult result,HttpServletRequest request,HttpServletResponse response);
}
