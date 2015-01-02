package com.github.ebnew.ki4so.core.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.ebnew.ki4so.core.dao.fs.FileSystemDao;
import com.github.ebnew.ki4so.core.exception.ParamsNotInitiatedCorrectly;

/**
 * 默认的key管理实现类，从classpath:/keys.js文件中
 * 读取key配置信息，是以json格式存储的。
 * @author Administrator
 *
 */
public class KeyServiceImpl extends FileSystemDao implements KeyService {
	
	private static Logger logger = Logger.getLogger(KeyServiceImpl.class.getName());
	
	/**
	 * 外部数据文件地址，优先级更高。
	 * (用户可以配置)
	 */
	public static final String  DEFAULT_EXTERNAL_DATA =  "D:\\workspace\\ki4so\\ki4so-core\\src\\main\\resources\\keys.js";
	
	/**
	 * 默认的数据文件地址，在classpath下。
	 */
	public static final String DEFAULT_CLASSPATH_DATA = "keys.js";
	 /**指定公钥存放文件路径 ，默认是classPath*/
    private static String PUBLIC_KEY_PATH = null;
    /**指定公钥存放文件名*/
    private static String PUBLIC_KEY_FILE = null;
    /** 密钥长度，用来初始化 */
    private static final int KEYSIZE = 1024;
    /** 指定加密算法为RSA */
    private static final String ALGORITHM = "RSA";
    private Key privateKey;
	
	/**
	 * 秘钥映射表，key是keyId,value是Key对象。
	 */
	private Map<String, Ki4soKey> keyMap = null;
	
	/**
	 * 秘钥映射表，key是appId,value是Key对象。
	 */
	private Map<String, Ki4soKey> appIdMap = null;
	
	public KeyServiceImpl(){
		this.externalData = DEFAULT_EXTERNAL_DATA;
		this.classPathData = DEFAULT_CLASSPATH_DATA;
		//加载数据。
		loadAppData();
	}
	
	@Override
	protected void loadAppData(){
		try{
			String s = this.readDataFromFile();
			//将读取的应用列表转换为应用map。
			List<Ki4soKey> keys = JSON.parseObject(s, new TypeReference<List<Ki4soKey>>(){});
			if(keys!=null){
				keyMap = new HashMap<String, Ki4soKey>(keys.size());
				appIdMap = new HashMap<String, Ki4soKey>(keys.size());
				for(Ki4soKey key:keys){
					keyMap.put(key.getKeyId(), key);
					appIdMap.put(key.getAppId(), key);
				}
				keys = null;
			}
		}catch (Exception e) {
			logger.log(Level.SEVERE, "load app data file error.", e);
		}
	}

	@Override
	public Ki4soKey findKeyByKeyId(String keyId) {
		Ki4soKey ki4soKey = null;
		loadAppData();	//重新加载数据
		if(this.keyMap!=null){
			ki4soKey = this.keyMap.get(keyId);
			try {
				String encryptKey = encryptKey(keyId,ki4soKey.getValue());	//私钥加密key
				ki4soKey.setValue(encryptKey);		//设置私钥加密后的key
			} catch (ParamsNotInitiatedCorrectly e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "public key file is not initiated！！！", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "cipher the key is wrong", e);
			}
		}
		return ki4soKey;
	}

	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#findKeyByAppId(java.lang.String)
	 */
	@Override
	public Ki4soKey findKeyByAppId(String appId) {
		Ki4soKey ki4soKey = null;
		loadAppData();	//重新加载数据
		if(this.appIdMap!=null){
			ki4soKey = this.appIdMap.get(appId);
			try {
			//公钥文件不存在
			if(!checkKeyFileExistByToken(appId)){
				generateKeyFile(appId);		//生成公钥文件
			}
			String encryptKey = encryptKey(appId,ki4soKey.getValue());	//私钥加密key
			ki4soKey.setValue(encryptKey);		//设置私钥加密后的key
			} catch (ParamsNotInitiatedCorrectly e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "public key file is not initiated！！！", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "cipher the key is wrong", e);
			}
		}
		return ki4soKey;
	}
	/**
	 * 使用公钥将key加密
	 * @param token 公钥文件标识
	 * @param keyValue 需要加密的key
	 * @return 加密后的key
	 */
	public String encryptKey(String token,String keyValue) throws ParamsNotInitiatedCorrectly,Exception{
		String encryptKey = null;
		if(checkKeyFileExistByToken(token)){	//判断公钥文件是否存在
			Key publicKey = loadPublicKey(); //加载公钥文件
	        /** 得到Cipher对象来实现对源数据的RSA加密 */
	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] bytes = keyValue.getBytes();
	        /** 执行公钥加密操作 */
	        byte[] encryptValue = cipher.doFinal(bytes);
	        //使用Base64加密
	        BASE64Encoder encoder = new BASE64Encoder();
	        encryptKey = encoder.encode(encryptValue);
		}else{
			throw new ParamsNotInitiatedCorrectly("The Public Key File Is Not Initiated !!!");
		}
		//返回加密后的key
		return encryptKey;
	}
	/**
	 * @return 用Base64加密后的私钥文件
	 * @throws Exception 
	 */
	public Key loadPublicKey(){
		Key publicKey = null;
        ObjectInputStream ois = null;
        try {
            /** 将文件中的公钥对象读出 */
            ois = new ObjectInputStream(new FileInputStream(
                    PUBLIC_KEY_FILE));
            publicKey = (Key) ois.readObject();
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
        return publicKey;
	}
	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#checkKeyFileExistByToken(java.lang.String)
	 */
	@Override
	public boolean checkKeyFileExistByToken(String token) {
		// TODO Auto-generated method stub
		Ki4soKey ki4soKey = appIdMap.get(token);		//获取当前运用的appId
		PUBLIC_KEY_PATH = ki4soKey.getKeyPath();		//获取公钥文件存放路径
		PUBLIC_KEY_FILE = PUBLIC_KEY_PATH + token;		//获取公钥文件名
		File keyFile = new File(PUBLIC_KEY_FILE);
		//公钥文件是否存在
		return keyFile.exists();
	}

	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#generateKeyFile()
	 */
	@Override
	public boolean generateKeyFile(String token) throws ParamsNotInitiatedCorrectly,Exception{
		// TODO Auto-generated method stub
		//判断运用ID列表是否为空
		if(appIdMap == null || 
				token == null ||
					"".equals(token)){
			throw new ParamsNotInitiatedCorrectly("appIdMap Parameter Is Initiated Incorrently !!");
		}
		
		 /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(KEYSIZE);
        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();
        /** 得到私钥 */
        privateKey = keyPair.getPrivate();
        
        ObjectOutputStream publicOutPutStream = null;
        try {
            /** 用对象流将生成的公钥写入文件 */
        	publicOutPutStream = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
            publicOutPutStream.writeObject(publicKey);
        } catch (Exception e) {
            return false;
        }
        finally{
            /** 清空缓存，关闭文件输出流 */
        	publicOutPutStream.close();
        }
        return true;
	}
}
