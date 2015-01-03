package com.github.ebnew.ki4so.core.key;

/**
 * 利用非对称加密保证key的安全性
 * @author Huiqiang lai
 *
 */

public interface KeyService {
	
	/**
	 * 根据密钥ID查找对应的密钥信息。
	 * @param keyId 密钥ID.
	 * @return 密钥信息。
	 */
	public Ki4soKey findKeyByKeyId(String keyId);
	
	/**
	 * 根据应用ID查找对应的密钥信息。
	 * @param appId 应用ID.
	 * @return 密钥信息。
	 */
	public Ki4soKey findKeyByAppId(String appId);

	/**
	 * 判断私钥文件是否已生成
	 * @param token 判断文件是否存在的标识
	 * @return true 表示私钥文件已生成；false 表示 私钥文件尚未生成
	 */
	public boolean checkKeyFileExistByToken(String token);
	/**
	 * 生成非对称加密文件(客户端生成私钥文件；服务器端生成公钥文件) 
	 * @param token 判断文件是否存在的标识
	 * @throws Exception 
	 * @return 返回信息
	 */
	public Object generateKeyFile(String token) throws Exception;
}
