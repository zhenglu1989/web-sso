package com.github.ebnew.ki4so.core.authentication.status;

import java.util.List;

/**
 * 用户登录状态存储器，实现了用户登录状态的存取方法
 * @author zhenglu
 * @since 15/4/28
 */
public interface KnightUserLoggedStatusStore {


    /**
     *  增加新的用户登录状态
     * @param userLoginStatus
     */
    public void addUserLoggerStatus(KnightUserLoginStatus userLoginStatus);

    /**
     * 删除用户登录状态
     * @param userId
     * @param appId
     */
    public void deleteUserLoginStatus(String userId,String appId);


    /**
     *  清楚某个用户所有的登录状态
     * @param userId
     */
    public void clearUpUserLoginStatus(String userId);


    /**
     *   根据用户标识查询所有的登录状态
     * @param userId
     * @return
     */
    public List<KnightUserLoginStatus> findUserLoginStatus(String userId);


}
