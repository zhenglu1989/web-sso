package com.github.ebnew.ki4so.core.service;

import com.github.ebnew.ki4so.core.app.KnightApp;
import com.github.ebnew.ki4so.core.app.KnightAppService;
import com.github.ebnew.ki4so.core.authentication.KnightAuthentication;
import com.github.ebnew.ki4so.core.authentication.KnightAuthenticationManager;
import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.authentication.status.KnightUserLoggedStatusStore;
import com.github.ebnew.ki4so.core.authentication.status.KnightUserLoginStatus;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenglu
 * @since 15/4/29
 */
public class KnightServiceImpl implements KnightService {
    private Logger logger = Logger.getLogger(KnightServiceImpl.class);

    private KnightAuthenticationManager authenticationManager;

    private KnightAppService appService;

    private KnightUserLoggedStatusStore userLoggedStatusStore;

    @Override
    public LoginResult login(KnightCredential credential) {
        //若没有凭据，则返回空
        if(credential == null){
            return null;
        }
        LoginResult result = new LoginResult();
        result.setSuccess(false);
        //调用认证处理器进行认证
        try{
            KnightAuthentication authentication  =  authenticationManager.authentication(credential);
            result.setSuccess(authentication != null?true :false);
            result.setAuthentication(authentication);

        }catch (Exception e){
            result.setCode("authentication failure");
            result.setMsgKey("authentiaction key");
            logger.error("authentication failure ::" + e.getMessage());

        }

        return result;
    }

    @Override
    public List<KnightApp> getAppList(KnightCredential credential) {
        List<KnightApp> apps = new ArrayList<KnightApp>();
        if(credential == null){
            return null;
        }
        try{
            KnightAuthentication authentication = authenticationManager.authentication(credential);
            if(authentication != null && authentication.getUser() != null){
                List<KnightUserLoginStatus> statusList =   this.userLoggedStatusStore.findUserLoginStatus(authentication.getUser().getId());
                //批量查询对应的应用信息
                if(statusList != null && statusList.size() >0){
                    for(KnightUserLoginStatus status :statusList){
                        KnightApp app = appService.findAppById(status.getAppId());
                        if(app != null){
                            apps.add(app);
                        }
                    }
                }
            }

        }catch (Exception e){
            logger.error("when find knightApp has error::"+e.getMessage());
        }

        return apps;
    }

    public void setAuthenticationManager(KnightAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAppService(KnightAppService appService) {
        this.appService = appService;
    }

    public void setUserLoggedStatusStore(KnightUserLoggedStatusStore userLoggedStatusStore) {
        this.userLoggedStatusStore = userLoggedStatusStore;
    }
}
