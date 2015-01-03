package com.github.ebnew.ki4so.client.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.github.ebnew.ki4so.core.exception.ParamsNotInitiatedCorrectly;
import com.github.ebnew.ki4so.core.key.KeyService;
import com.github.ebnew.ki4so.core.key.Ki4soKey;

/**
 * 默认的秘钥信息获取实现类，该类只是一个简单的实现，非常不安全。 在生产环境，建议请使用公钥和私钥的方式对秘钥信息
 * 进行加密，避免秘钥在公网环境下泄露。请自行加强安全性。
 * 
 * @author Administrator
 */
@SuppressWarnings("deprecation")
public class DefaultKeyServiceImpl implements KeyService {

	private static Logger logger = Logger.getLogger(DefaultKeyServiceImpl.class
			.getName());

	private String ki4soServerFetchKeyUrl;
	 /** 指定私钥存放文件 ，默认是classPath*/
    private static String PRIVATE_KEY_PATH = null;
    /**私钥文件名*/
    private static String PRIVATE_KEY_FILE = "PrivateKey";
    /** 指定加密算法为RSA */
    private static final String ALGORITHM = "RSA";
	/**
	 * 本应用的秘钥信息。
	 */
	private Ki4soKey ki4soKey;

	/**
	 * 本应用的应用id.
	 */
	private String appId;

	private static DefaultHttpClient httpClient = new DefaultHttpClient();
	
	//默认无参构造方法
	public DefaultKeyServiceImpl(){
		
	}
	
	public DefaultKeyServiceImpl(String ki4soServerFetchKeyUrl, String appId) {
		super();
		this.ki4soServerFetchKeyUrl = ki4soServerFetchKeyUrl;
		this.appId = appId;
	}

	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#findKeyByAppId(java.lang.String)
	 */
	@Override
	public Ki4soKey findKeyByAppId(String appId) {
		if (ki4soKey == null) {
			// do fetch key from remote server.
			this.ki4soKey = fetchKeyFromKi4soServer();
		}
		return ki4soKey;
	}

	private Ki4soKey fetchKeyFromKi4soServer() {
		HttpPost httpPost = null;
		Ki4soKey ki4so = null;
		try {
			httpPost = new HttpPost(ki4soServerFetchKeyUrl);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("appId", this.appId));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
				ki4so = JSON.parseObject(content, Ki4soKey.class);
				return ki4so;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "fetch ki4so key from server error, the url is ["+ki4soServerFetchKeyUrl+"]", e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#findKeyByKeyId(java.lang.String)
	 */
	@Override
	public Ki4soKey findKeyByKeyId(String keyId) {
		if (ki4soKey == null) {
			return this.findKeyByAppId(null);
		}
		return ki4soKey;
	}
	/**
	 * @param keyValue 需解密的key字符串
	 * @param token 标识privateKey
	 * @return 解密后的key
	 */
	public String decryptKey(String token,String keyValue){
		String decryptKey = null;	//解密后的密文
		//检测私钥文件是否存在
		if(!checkKeyFileExistByToken(token)){
			throw new ParamsNotInitiatedCorrectly("The Private Key File Is Not Initiated !!!");
		}
		Key privateKey = loadPrivateKey();		//加载文件私钥
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes = decoder.decodeBuffer(keyValue);
	        /** 执行解密操作 */
	        byte[] decryptValue = cipher.doFinal(bytes);
	        decryptKey = new String(decryptValue);		//decryptValue[]转换成String
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.WARNING, "Cipher error used by private key !!", e);
		} 
        
		return decryptKey;
	}
	/**
	 * @return 用Base64加密后的私钥文件
	 * @throws Exception 
	 */
	public Key loadPrivateKey(){
		Key privateKey = null;
        ObjectInputStream ois = null;
        try {
            /** 将文件中的私钥对象读出 */
            ois = new ObjectInputStream(new FileInputStream(
                    PRIVATE_KEY_FILE));
            privateKey = (Key) ois.readObject();
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
        return privateKey;
	}
	
	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#checkKeyFileExistByToken(java.lang.String)
	 */
	@Override
	public boolean checkKeyFileExistByToken(String token) {
		// TODO Auto-generated method stub
		Properties property = new Properties();
		//初始化文件输入流
		InputStream in = null;
		try {
			in = DefaultKeyServiceImpl.class.getResourceAsStream("/keySecurity.properties");
			property.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.log(Level.WARNING, "keySecurity File Is Not Found,The Filepath Info Read Fail!", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(Level.WARNING, "keySecurity File Load Fail!", e);
		}
		//从配置文件中获取私钥存放位置
		PRIVATE_KEY_PATH = property.getProperty("keyPath");
		//配置文件中指定私钥的存放位置
		if(PRIVATE_KEY_PATH != null || !"".equals(PRIVATE_KEY_PATH)){
			PRIVATE_KEY_FILE = PRIVATE_KEY_PATH + PRIVATE_KEY_FILE;
		}
		File file = new File(PRIVATE_KEY_FILE);
		
		return file.exists();
	}

	/* (non-Javadoc)
	 * @see com.github.ebnew.ki4so.core.key.KeyService#generateKeyFile()
	 */
	@Override
	public Object generateKeyFile(String token) throws Exception{
		// TODO Auto-generated method stubj
		String keyFile = null;		//获取服务器端生成秘钥
		HttpPost httpPost = null;
		//判断运用ID列表是否为空
		if(token == null || "".equals(token)){
			throw new ParamsNotInitiatedCorrectly("Token Parameter Is Initiated Incorrently !!");
		}
		//私钥文件已存在
		if(checkKeyFileExistByToken(token)){
			return false; 
		}
		try {
			httpPost = new HttpPost(ki4soServerFetchKeyUrl);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("appId", this.appId));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				keyFile = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "fetch ki4so key from server error, the url is ["+ki4soServerFetchKeyUrl+"]", e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		//用Base64解密私钥
		@SuppressWarnings("restriction")
		BASE64Decoder decoder = new BASE64Decoder();
	    @SuppressWarnings("restriction")
		byte[] bytes = decoder.decodeBuffer(keyFile);
		keyFile = new String(bytes);  	
		//将私钥写入客户端
        ObjectOutputStream keyOutPutStream = null;
        try {
            /** 用对象流将生成的密钥写入文件 */
            keyOutPutStream = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
            keyOutPutStream.writeObject(keyFile);
        } catch (Exception e) {
            throw e;
        }
        finally{
            /** 清空缓存，关闭文件输出流 */
        	keyOutPutStream.close();
        }
        return true;
	}
}