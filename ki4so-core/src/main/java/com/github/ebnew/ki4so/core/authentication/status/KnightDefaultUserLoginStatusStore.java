package com.github.ebnew.ki4so.core.authentication.status;

import com.github.ebnew.ki4so.common.utils.StringUtils;

import java.util.*;

/**
 * 用户登录状态存储器 默认实现类
 * @author zhenglu
 * @since 15/4/28
 */
public class KnightDefaultUserLoginStatusStore implements KnightUserLoggedStatusStore {


    /**
     * 用户登录状态,不允许重复状态值
     */
    private Set<KnightUserLoginStatus> loginStatuses = new HashSet<KnightUserLoginStatus>();


    /**
     * 用户标识和用户状态列表质检的映射表，相当于一个索引，方便根据用户标识查询所有的登录状态标。
     * 其中map中key为用户标识，value是该用户所有的登录状态列表
     */
    private Map<String,List<KnightUserLoginStatus>> userIndexMap = new HashMap<String, List<KnightUserLoginStatus>>();

    public Set<KnightUserLoginStatus> getLoginStatuses() {
        return loginStatuses;
    }

    public Map<String, List<KnightUserLoginStatus>> getUserIndexMap() {
        return userIndexMap;
    }

    @Override
    public synchronized void addUserLoggerStatus(KnightUserLoginStatus userLoginStatus) {

        if(userLoginStatus != null && !StringUtils.isEmpty(userLoginStatus.getUserId()) && !StringUtils.isEmpty(userLoginStatus.getAppId())){
            if(userLoginStatus.getLoginDate() == null){
                userLoginStatus.setLoginDate(new Date());
            }
            this.loginStatuses.add(userLoginStatus);
            List<KnightUserLoginStatus> list = this.userIndexMap.get(userLoginStatus.getUserId());
            if(list == null){
                list = new ArrayList<KnightUserLoginStatus>();
                this.userIndexMap.put(userLoginStatus.getUserId(),list);
            }
            list.add(userLoginStatus);
        }

    }

    @Override
    public synchronized void deleteUserLoginStatus(String userId, String appId) {
        //验证数据合法性
        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(appId)){
            KnightUserLoginStatus status = new KnightUserLoginStatus();
            status.setUserId(userId);
            status.setAppId(appId);
            this.loginStatuses.remove(status);
            List<KnightUserLoginStatus> list = this.userIndexMap.get(userId);
            if(list != null){
                list.remove(status);
            }
        }

    }

    @Override
    public synchronized void clearUpUserLoginStatus(String userId) {
        if(!StringUtils.isEmpty(userId)){
            List<KnightUserLoginStatus> list = this.userIndexMap.get(userId);
            if(list != null){
                list.clear();
                this.userIndexMap.put(userId,null);
            }
        }

    }

    @Override
    public List<KnightUserLoginStatus> findUserLoginStatus(String userId) {
        if(!StringUtils.isEmpty(userId)){
            return this.userIndexMap.get(userId);
        }
        return null;
    }
}
