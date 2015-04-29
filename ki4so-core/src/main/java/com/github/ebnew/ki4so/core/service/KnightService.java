package com.github.ebnew.ki4so.core.service;

import com.github.ebnew.ki4so.core.app.KnightApp;
import com.github.ebnew.ki4so.core.authentication.KnightCredential;

import java.util.List;

/**
 * 核心服务接口，定义了所有的核心方法
 * 该接口是一个门面类，也定义了对外提供的方法
 * @author zhenglu
 * @since 15/4/29
 */
public interface KnightService {

    /**
     * 利用用户凭据登录knight中心服务
     * @param credential
     * @return
     */
     public LoginResult login(KnightCredential credential);

    /**
     * 获得
     * @param credential
     * @return
     */

     public List<KnightApp> getAppList(KnightCredential credential);

}
