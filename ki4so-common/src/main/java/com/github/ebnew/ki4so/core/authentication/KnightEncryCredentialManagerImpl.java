package com.github.ebnew.ki4so.core.authentication;

import com.alibaba.fastjson.JSON;
import com.github.ebnew.ki4so.common.KnightBase64Coder;
import com.github.ebnew.ki4so.common.KnightDECoder;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.key.KnightKey;
import com.github.ebnew.ki4so.core.key.KnightKeyService;
import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;
import org.apache.log4j.Logger;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密凭据管理的实现类
 * @author zhenglu
 * @since 15/4/27
 */
public class KnightEncryCredentialManagerImpl implements KnightEncryCredentialManager {

    private static final Logger logger = Logger.getLogger(KnightEncryCredentialManagerImpl.class);

    private KnightKeyService keyService;

    public void setKeyService(KnightKeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    public KnightCredentialInfo decrypt(KnightEncryCredential encryCredential) {
        if(encryCredential != null && !StringUtils.isEmpty(encryCredential.getCredential())){
            String credential = encryCredential.getCredential();
            return parseEncryCredentital(credential);
        }
        return null;
    }

    /**
     * 编码的实现流程如下：
     *  1.将加密凭据信息的敏感字段包括 userId,createTime,expiredTime字段
     *  组合成json格式的数据，然后试用密钥对改字符串进行des加密，再将加密后的字符串通过base64编码
     *  2.将上述加密串与其他非敏感的信息进行拼接，格式如下:[敏感信息加密串]?appId = 1&keyId=2
     *  其中敏感信息加密串为第一步得到的结果，appid为应用标识符，keyId为密钥标识
     *  3.试用url进行编码，反之tomcat7下报cookie错误
     *
     * @param credentialInfo
     * @return
     */
    public String encrypt(KnightCredentialInfo credentialInfo) {
        StringBuffer buffer = new StringBuffer();
        if(credentialInfo != null){
            try {
                String data = encryptSensitiveInfo(credentialInfo);
                buffer.append(data).append("appId=").append(credentialInfo.getAppId()).append("&keyId=").append(credentialInfo.getKeyId());
                return URLEncoder.encode(KnightBase64Coder.encryptBase64(buffer.toString().getBytes()),"utf-8");


            }catch (Exception e){

                logger.error("对加密凭据加密失败 error message ::" +e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean checkEncryCredentialInfo(KnightCredentialInfo credentialInfo) {
        if(credentialInfo != null){
            //无凭据对应的用户标识，则无效
                if(!StringUtils.isEmpty(credentialInfo.getUserId())) {

                    return false;
                }
              Date now = new Date();
              if(credentialInfo.getExpireTime() != null){
                 long data = credentialInfo.getExpireTime().getTime() - now.getTime();
                  if(data > 0 ){
                      return true;
                  }
              }
            }
        return false;
    }

    private KnightCredentialInfo parseEncryCredentital(String credential){
        KnightCredentialInfo info = new KnightCredentialInfo();
        try {
            credential = URLDecoder.decode(credential,"utf-8");
            credential = new String(KnightBase64Coder.decryptBase64(credential));
            String[] items = credential.split("\\?");
            if(items.length == 2){
                //如果第二个字符串不为空，先解析第二个字符串
                if(items[1] != null && items[1].length() > 0 ){
                    //使用&分割字符串
                    String[] params = items[1].split("&");
                    for (String param : params){
                        if(!StringUtils.isEmpty(param)){
                            String[] values = param.split("=");
                            if(values != null && values.length == 2){

                                if("appId".equalsIgnoreCase(values[0])){
                                    info.setAppId(values[1]);
                                }
                                if("keyId".equalsIgnoreCase(values[0])){
                                    info.setKeyId(values[1]);
                                }
                            }

                        }

                    }

                }
            }
            if(!StringUtils.isEmpty(items[0])){
                byte[] data = KnightBase64Coder.decryptBase64(items[0]);
                 KnightKey key =  keyService.findKeyByKeyId(info.getKeyId());
                if(key != null){
                    byte[] origin = KnightDECoder.decrypt(data,key.toSecurityKey());
                    String json = new String(origin);
                    Map map = (Map)JSON.parse(json);
                    if(map != null) {
                        Object userId = map.get("userId");
                        Object createTime = map.get("createTime");
                        Object expiredTime = map.get("expireTime");
                        info.setUserId(userId == null?"":String.valueOf(userId));
                        info.setCreateTime(createTime == null ? null :new Date(Long.valueOf(createTime.toString())));
                        info.setExpireTime(expiredTime == null ? null:new Date(Long.valueOf(expiredTime.toString())));
                    }
                 }
            }


        }catch (Exception e){
            logger.error("parse encry credential exception ::"  + e.getMessage());
            e.printStackTrace();

        }
        return info;

    }

    /**
     * 解析加密后的凭据信息为凭据对象,过程与加密过程相反的逆过程
     * @param info
     * @return
     * @throws Exception
     */
    private String encryptSensitiveInfo(KnightCredentialInfo info) throws Exception{

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",info.getUserId());
        if(info.getCreateTime() != null){
            map.put("createTime",info.getCreateTime());
        }
        if(info.getExpireTime() != null){
            map.put("expiredTime",info.getExpireTime());

        }
        KnightKey knightKey = keyService.findKeyByKeyId(info.getKeyId());
        if(knightKey != null){
            Key key = knightKey.toSecurityKey();
            if(key != null){
              byte[] data =   KnightDECoder.encrypt(JSON.toJSONBytes(map),key);
              return KnightBase64Coder.encryptBase64(data);
            }
        }
        return "";

    }
}
