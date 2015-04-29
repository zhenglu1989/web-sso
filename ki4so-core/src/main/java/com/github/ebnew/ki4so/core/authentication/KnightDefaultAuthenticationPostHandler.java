package com.github.ebnew.ki4so.core.authentication;

import com.github.ebnew.ki4so.core.app.KnightApp;
import com.github.ebnew.ki4so.core.app.KnightAppService;
import com.github.ebnew.ki4so.core.authentication.status.KnightUserLoggedStatusStore;
import com.github.ebnew.ki4so.core.authentication.status.KnightUserLoginStatus;
import com.github.ebnew.ki4so.core.key.KnightKey;
import com.github.ebnew.ki4so.core.key.KnightKeyService;
import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;
import com.github.ebnew.ki4so.web.utils.WebConstants;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的后置处理器，提供抽象的方法由具体的子类实现
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightDefaultAuthenticationPostHandler implements KnightAuthenticationPostHandler{

    private static final Logger logger = Logger.getLogger(KnightDefaultAuthenticationPostHandler.class);
    /**
     *   服务端过期时间设置为3个月
     */

    private static final Long SERVER_INVALID = 3L*30*24*60*60*1000;

    /**
     * 客户端失效时间为1分钟
     */
    private static final Long CLIENT_INVALID = 60L * 1000;


    private KnightEncryCredentialManager encryCredentialManager;

    private KnightKeyService keyService;

    private KnightAppService appService;

    private KnightUserLoggedStatusStore userLoggedStatusStore;




    @Override
    public KnightAuthentication postAuthentication(KnightCredential credential, KnightUser user) {
        Date createTime = new Date();
        // 若认证通过，则返回认证的结果对象
        KnightAuthenticationImpl authentication = new KnightAuthenticationImpl();
        authentication.setAuthenticatedDate(createTime);
        authentication.setAuthenticatedDate(createTime);
        authentication.setUser(user);
        encryCredentialWithServerKey(authentication, credential, user);
        encryCredentialWithAppKey(authentication, credential, user);
        return authentication;
    }

    /**
     * 使用ki4so服务器本身的key对凭据进行加密处理
     * @param authentication
     * @param credential
     * @param user
     */
    private void encryCredentialWithServerKey(KnightAuthenticationImpl authentication,KnightCredential credential,KnightUser user){
        //如果是原始凭据，则需要进行加密操作处理
        if(credential != null && credential.isOriginal()){
            //查找自身服务对应的应用信息
            KnightApp app =  appService.findKi4soServerApp();
            if(app == null ){
                logger.info("no ki4so key info");

            }
           String encryStr =  encryCredentialManager.encrypt(buildEncryCredentialInfo(app.getAppId(),authentication,user,SERVER_INVALID));
            Map<String,Object> attributes = authentication.getAttrbutes();
            if(attributes == null){
                attributes = new HashMap<String, Object>();
            }
            attributes.put(KNIGHT_SERVER_EC_KEY,encryStr);
            authentication.setAttributes(attributes);

        }

    }

    private void encryCredentialWithAppKey(KnightAuthenticationImpl authentication,KnightCredential credential,KnightUser user){
        KnightAbstractParameter abstractParameter = null;
        if(credential != null && credential instanceof KnightAbstractParameter){
            abstractParameter = (KnightAbstractParameter)credential;
        }
        //若登录对应的服务参数service的值不为空，则使用该service对应的应用的key进行加密
        if(authentication != null && abstractParameter != null && abstractParameter.getParameterValue(WebConstants.SERVICE_PARAM_NAME)!=null){
            String service = abstractParameter.getParameterValue(WebConstants.SERVICE_PARAM_NAME).toString().trim().toLowerCase();
            //service 不为空，且符合http协议url格式，则继续加密
            if(service.length() > 0){
                KnightApp app = appService.findAppByHost(service);
                if(app != null){
                    String encryCredential = encryCredentialManager.encrypt(buildEncryCredentialInfo(app.getAppId(),authentication,user,CLIENT_INVALID));
                    Map<String,Object> attributes = authentication.getAttrbutes();
                    if(attributes == null){
                        attributes = new HashMap<String, Object>();
                    }
                    attributes.put(KNIGHT_CLIENT_EC_KEY,encryCredential);
                    attributes.put(WebConstants.SERVICE_PARAM_NAME,service);
                    authentication.setAttributes(attributes);
                    //更新用户登录状态到存储器中
                    KnightUserLoginStatus status = new KnightUserLoginStatus();
                    status.setAppId(app.getAppId());
                    status.setUserId(user.getId());
                    status.setLoginDate(authentication.getAuthenticateDate());
                    userLoggedStatusStore.addUserLoggerStatus(status);

                }
            }
        }


    }

    private KnightCredentialInfo buildEncryCredentialInfo(String appId,KnightAuthenticationImpl authentication,KnightUser user,long duration){
        KnightCredentialInfo info = new KnightCredentialInfo();
        if(authentication == null || user == null){
            return info;
        }
        KnightKey key =  keyService.findKeyByAppId(appId);
        if(key == null){
            logger.info("no key for appId:: " + appId);
        }
        info.setAppId(appId);
        info.setCreateTime(authentication.getAuthenticateDate());
        info.setUserId(user.getId());
        info.setKeyId(key.getKeyId());
        Date expireTime = new Date(authentication.getAuthenticateDate().getTime() + duration);
        info.setExpireTime(expireTime);
        return info;

    }




    public void setEncryCredentialManager(KnightEncryCredentialManager encryCredentialManager) {
        this.encryCredentialManager = encryCredentialManager;
    }

    public void setKeyService(KnightKeyService keyService) {
        this.keyService = keyService;
    }

    public void setAppService(KnightAppService appService) {
        this.appService = appService;
    }

    public void setUserLoggedStatusStore(KnightUserLoggedStatusStore userLoggedStatusStore) {
        this.userLoggedStatusStore = userLoggedStatusStore;
    }
}
