package com.github.ebnew.ki4so.core.authentication;

import java.util.Map;

/**
 * 参数，定义了获得动态参数列表的接口
 * @author zhenglu
 * @since 15/4/27
 */
public interface KnightParameter {

    /**
     * 通过参数名获得参数值的方法
     * @param parameName
     * @return
     */
    public Object getParameterValue(String parameName);


    /**
     * 获得所有的参数
     * @return 所有的参数列表
     */
    public Map<String,Object> getParameters();

    /**
     * 设置参数列表
     * @param parameters
     */

    public void setParameters(Map<String,Object> parameters);
}
