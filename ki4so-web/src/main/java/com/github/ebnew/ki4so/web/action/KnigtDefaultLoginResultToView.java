package com.github.ebnew.ki4so.web.action;

import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.authentication.KnightAuthentication;
import com.github.ebnew.ki4so.core.authentication.KnightAuthenticationPostHandler;
import com.github.ebnew.ki4so.core.authentication.handlers.AuthenticationHandler;
import com.github.ebnew.ki4so.core.message.MessageUtils;
import com.github.ebnew.ki4so.core.service.LoginResult;
import com.github.ebnew.ki4so.web.utils.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 默认的实现类
 * @author zhenglu
 * @since 15/4/30
 */
public class KnigtDefaultLoginResultToView  implements  KnightLoginResultToView{
    @Override
    public ModelAndView loginResultToView(ModelAndView mav, LoginResult result, HttpServletRequest request, HttpServletResponse response) {
        //若登录成功，则返回成功页面
        if(mav == null){
            mav = new ModelAndView();
        }
        if(result == null || request == null || response == null){
            return  mav;
        }
        if(result.isSuccess()){
            //登录结果对象
            KnightAuthentication authentication =  result.getAuthentication();
            request.getSession().removeAttribute(WebConstants.KI4SO_SERVICE_KEY_IN_SESSION);
            //knight服务端加密的凭据存在，则写入cookie中
            if(authentication != null && authentication.getAttrbutes() != null){
                Map<String,Object> attributes = authentication.getAttrbutes();
                //knight服务端加密的凭据存在，则写入cookie
                if(attributes.get(KnightAuthenticationPostHandler.KNIGHT_SERVER_EC_KEY) != null){
                   response.addCookie(new Cookie(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY,attributes.get(KnightAuthenticationPostHandler.KNIGHT_SERVER_EC_KEY).toString()));
                }
                //knight客户端加密的凭据和参数service存在，则跳转到对应的页面中
                if(attributes.get(KnightAuthenticationPostHandler.KNIGHT_CLIENT_EC_KEY) != null && !StringUtils.isEmpty(attributes.get(WebConstants.SERVICE_PARAM_NAME).toString())){
                    mav.getModel().put("authentication",authentication);
                    mav.setView(this.buildRedirectView(attributes.get(WebConstants.SERVICE_PARAM_NAME).toString(),attributes.get(KnightAuthenticationPostHandler.KNIGHT_CLIENT_EC_KEY).toString()));
                    return mav;

                }
            }
            mav.getModel().put("authentication",authentication);
            mav.setViewName("loginSuccess");

        }else{
            //删除以前不合法的凭据信息
            //清除cookie值
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie:cookies){
                if(WebConstants.KI4SO_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY.equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            mav.getModel().put("code",result.getCode());
            mav.getModel().put("msg", MessageUtils.getMessage(result.getMsgKey()));
        }

        return mav;
    }
    private RedirectView buildRedirectView(String service,String encryCredentital){
        StringBuffer buffer = new StringBuffer(service);
        if(service.contains("?")){
            buffer.append("&");
        }else {
            buffer.append("?");
        }
        buffer.append(WebConstants.KI4SO_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY).append("=").append(encryCredentital);
        return new RedirectView(buffer.toString());
    }
}
