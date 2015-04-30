package com.github.ebnew.ki4so.core.authentication;

import com.github.ebnew.ki4so.core.authentication.handlers.AuthenticationHandler;
import com.github.ebnew.ki4so.core.authentication.resolvers.CredentialToPrincipalResolver;
import com.github.ebnew.ki4so.core.exception.UnsupportedCredentialsException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightAuthenticationManagerImpl implements KnightAuthenticationManager {

    public static final Logger logger = Logger.getLogger(KnightAuthenticationManagerImpl.class);


    /**
     * 认证处理器集合，使用多个认证处理器对凭证逐一认证，只要有一个通过即可
     */
    private List<AuthenticationHandler> authenticationHandlers;

    private List<CredentialToPrincipalResolver> credentialToPrincipalResolvers;
    /**
     * 认证成功后处理对象。
     */
    private KnightAuthenticationPostHandler authenticationPostHandler;



    @Override
    public KnightAuthentication authentication(KnightCredential credential) {
        //是否找到支持的凭据认证处理器
        boolean foundSupported = false;
        //是否认证通过
        boolean authenticated = false;
        if(credential == null){
            logger.info("credential is null");
            return null;
        }
        for(AuthenticationHandler handler : authenticationHandlers){
            if(handler.supports(credential)){
                foundSupported = true;
                authenticated =  handler.authenticate(credential);
                if(authenticated){
                    break;
                }
            }
        }
        //
        boolean foundCrentialResolve = false;
        KnightUser user = null;
        if(foundSupported && authenticated){

            for(CredentialToPrincipalResolver resolver:credentialToPrincipalResolvers){
                //用户凭据解析器是否支持该凭据
                if(resolver.supports(credential)){
                   foundCrentialResolve = true;
                   user =  resolver.resolvePrincipal(credential);
                    //若解析成功，则跳出循环
                    if(user != null){
                        break;
                    }
                }
            }

        }
        if(!foundCrentialResolve){
            logger.error("not found any supported credentials resolvers");
            throw new UnsupportedCredentialsException();
        }


        return authenticationPostHandler.postAuthentication(credential,user);
    }

    public void setAuthenticationHandlers(List<AuthenticationHandler> authenticationHandlers) {
        this.authenticationHandlers = authenticationHandlers;
    }

    public void setCredentialToPrincipalResolvers(List<CredentialToPrincipalResolver> credentialToPrincipalResolvers) {
        this.credentialToPrincipalResolvers = credentialToPrincipalResolvers;
    }

    public void setAuthenticationPostHandler(KnightAuthenticationPostHandler authenticationPostHandler) {
        this.authenticationPostHandler = authenticationPostHandler;
    }
}
