package com.github.ebnew.ki4so.core.app;

/**
 * 应用信息的服务类
 * @author zhenglu
 * @since 15/4/26
 */
public interface KnightAppService {

    /**
     * 通过appid查找对应的应用信息
     * @param appId
     * @return
     */

    public KnightApp findAppById(String appId);

    /**
     * 查找系统中Ki4so服务对应的应用信息
     * @return
     */

    public KnightApp findKi4soServerApp();

    /**
     * 通过host查找对应的应用信息
     * @param host
     * @return
     */

    public KnightApp findAppByHost(String host);
}
