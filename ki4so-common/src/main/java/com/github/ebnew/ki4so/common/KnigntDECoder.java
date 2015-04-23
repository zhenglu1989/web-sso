package com.github.ebnew.ki4so.common;

import sun.util.resources.CurrencyNames_iw_IL;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author zhenglu
 * @since 15/4/23
 */
public class KnigntDECoder {

    /**
     *
     * 密钥算法
     */
    private static final String KEY_ALGORIHM = "DESede";

    private static final String DEFAULT_CIPHER_ALGORIHM ="DESede/ECB/ISO10126Padding";

    private static final int KEY_SITE = 168;


    /**
     * 初始化密钥
     * @param seed
     * @return
     * @throws Exception
     */
    public static Key initSecretKey(String seed) throws Exception{
        //返回生成指定算法的密钥 KeyGenerator
        KeyGenerator keyGenerator =   KeyGenerator.getInstance(KEY_ALGORIHM);
        //初始化此密钥生成器，使其具有确定的密钥大小
        SecureRandom secureRandom =  SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes());
        keyGenerator.init(KEY_SITE,secureRandom);

        //生成一个密钥
        SecretKey secretKey  = keyGenerator.generateKey();
        //实例化DES 密钥规则
        DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(secretKey.getEncoded());
        //实例化密钥工厂
        SecretKeyFactory secretKeyFactory =  SecretKeyFactory.getInstance(KEY_ALGORIHM);
        //生成密钥
        return secretKeyFactory.generateSecret(deSedeKeySpec);
    }

    public static byte[] encrypt(byte[] data,Key key) throws Exception{
        return encrypt(data,key,DEFAULT_CIPHER_ALGORIHM);
    }

    /**
     *
     * @param data 原始数据
     * @param key  密钥
     * @param algorithm 加密算法、工作模式、填充模式
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,Key key,String algorithm) throws Exception{
        Cipher cipher =    Cipher.getInstance(algorithm);


        /**
         *
         *   DECRYPT_MODE 用于将 cipher 初始化为解密模式的常量
         *
         *   PRIVATE_KEY 用于指示要打开的密钥为“私钥”的常量。
         *
         *   PUBLIC_KEY 用于指示要打开的密钥为“公钥”的常量。
         *
         *   SECRET_KEY 用于指示要打开的密钥为“秘密密钥”的常量。
         *
         *   UNWRAP_MODE 用于将 cipher 初始化为密钥打开模式的常量。
         *
         *   WRAP_MODE 用于将 cipher 初始化为密钥包装模式的常量
          */
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data,Key key,String algorithm) throws Exception{
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal();
    }

    public static byte[] decrypt(byte[] data,Key key) throws Exception{
        return decrypt(data,key,DEFAULT_CIPHER_ALGORIHM);
    }




}
