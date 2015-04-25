package com.github.ebnew.ki4so.core.key;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.dao.fs.KnightFileSystemDao;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的key管理实现类，从classpath:/keys.js文件中
 * 读取key配置信息，是以json格式存储的
 * @author zhenglu
 * @since 15/4/24
 */
public class KnightKeyServiceImpl extends KnightFileSystemDao implements KnightKeyService{

   private static final Logger logger = Logger.getLogger(KnightKeyServiceImpl.class);

   public static final String DEFALUT_EXTERNAL_DATA = "";

   public static final String DEFALUT_CLASSPATH_DATA = "keys.js";

   private static String PUBLIC_KEY_PATH = null;

    private static String PUBLIC_KEY_FILE = null;

    private static final String ALGORITHM = "RSA";

    private static final int KEY_SIZE = 1024;

    /**
     * 密钥映射表 key是keyId，value是key对象
     */
    private Map<String,KnightKey> keyMap = new HashMap<String, KnightKey>();

    /**
     * 密钥映射表，key是appid，value是Key对象
     */
    private Map<String,KnightKey> appIdMap = new HashMap<String, KnightKey>();

    protected void init(){
        this.externalData = DEFALUT_EXTERNAL_DATA;
        this.classPathData = DEFALUT_CLASSPATH_DATA;
        loadAppData();
    }

    @Override
    protected void loadAppData() {
        try{
            String content = this.readDataFromFile();
            // 将读取的应用列表转换为应用map
            List<KnightKey> keyList = JSON.parseObject(content,new TypeReference<List<KnightKey>>(){});
            for(KnightKey key:keyList){
                keyMap.put(key.getKeyId(),key);
                appIdMap.put(key.getAppId(),key);
            }

        }catch (Exception e){
            logger.error("loadAppData has error " + e.getMessage());
        }


    }

    @Override
    public KnightKey findKeyByKeyId(String keyId) {
        KnightKey key = null;
        if(!StringUtils.isEmpty(keyId)){
            return this.keyMap.get(keyId);
        }

        return null;
    }

    @Override
    public KnightKey findKeyByAppId(String appId) {
        KnightKey key = null;
        if(!StringUtils.isEmpty(appId)){
            return this.appIdMap.get(appId);
        }

        return null;
    }

    /**
     * 用Base64加密后的私钥文件
     * @return
     */
    public Key loadPublicKey(){
        Key keyPublic = null;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            keyPublic = (Key)inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keyPublic;
    }

    /**
     * 对公钥进行base64加密
     * @param appId
     * @param keyValue
     * @return
     */
    public String encryptKey(String appId,String keyValue){
        if(checkKeyFileExistByToken(appId)){ //判断公钥文件是否存在
            Key publicKey = loadPublicKey(); //加载公钥文件
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);

                cipher.init(Cipher.ENCRYPT_MODE,publicKey);
                byte[] bytes = keyValue.getBytes();
               byte[] encryptValue =  cipher.doFinal(bytes);
                BASE64Encoder encoder = new BASE64Encoder();
                String encryptKey = encoder.encode(encryptValue);
                return encryptKey;

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    public boolean checkKeyFileExistByToken(String appId) {
        if(!StringUtils.isEmpty(appId)){
          KnightKey key = appIdMap.get(appId);
          PUBLIC_KEY_PATH =  key.getKeyPath(); //获取公钥文件的存放路径
          PUBLIC_KEY_FILE = PUBLIC_KEY_PATH + appId;
           File file = new File(PUBLIC_KEY_FILE);
            return file.exists();
        }
        return false;
    }

    /**
     * 生成密钥,将公钥写入文件,返回私钥
     * @param token
     * @return
     */
    @Override
    public Object generateKeyFile(String token) {
       if(appIdMap == null || StringUtils.isEmpty(token)){
           logger.info("appIdMap Parameter Is Initiated Incorrently !!");
           return null;
       }
      // RSA算法要求有一个可信任的随机源
        SecureRandom random = new SecureRandom();
        ObjectOutputStream outputStream = null;
        Key privateKey = null;

        try {
            //为RSA算法创建一个keyPairGenerator对象
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            //利用上面的随机数据源初始化这个keyPairGenerator
            keyPairGenerator.initialize(KEY_SIZE,random);
            //生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 得到公钥
            Key publicKey = keyPair.getPublic();
            //得到私钥
             privateKey  = keyPair.getPrivate();

            outputStream = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
            /** 用对象流将公钥写入文件 **/
            outputStream.writeObject(publicKey);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            /** 清空buffer缓存，关闭文件输出流**/
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return privateKey;


    }
}
