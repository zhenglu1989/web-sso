package com.github.ebnew.ki4so.core.key;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;

/**
 * rsa 密钥工具类
 * @author zhenglu
 * @since 15/4/25
 */
public class KnightRSASecurityUtil {


    /**
     * 置顶密钥算法 为rsa
     */
    private static final String ALGORITHM = "RSA";


    // 密钥长度
    private static final int KEYSIZE = 1024;

    /**
     * 公钥存放文件名
     */
    private static final String PUBLIC_KEY_FILE ="1001";

    //私钥存放文件名
    private static final String PRIVATE_KEY_FILE="PrivateKey";


    public static void generateKeyPair(){
         /** rsa算法要求有一个可信任的随机源**/
        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = null;
        ObjectOutputStream publicOutput = null;
        ObjectOutputStream privateOutput = null;

        try {
             generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEYSIZE,random);
            KeyPair pair = generator.generateKeyPair();
            //得到私钥
            PrivateKey privateKey =  pair.getPrivate();
            //得到公钥
            PublicKey publicKey =   pair.getPublic();
             publicOutput = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
             privateOutput = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
            //将公钥私钥写出文件
            publicOutput.writeObject(publicKey);
            privateOutput.writeObject(privateKey);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(publicOutput != null){
                    publicOutput.close();
                }
                if(privateOutput != null){
                    privateOutput.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    /**
     *  加密数据源
     * @param source
     * @return
     */

    public static String encrypt(String source){
        generateKeyPair();
        Key publicKey = null;
        ObjectInputStream inputStream = null;
        try{
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            publicKey = (Key)inputStream.readObject();

        }catch (Exception e){
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

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] bytes = source.getBytes();
            //执行加密算法
            byte[] encodebyte = cipher.doFinal(bytes);
            BASE64Encoder encoder = new BASE64Encoder();
            //对密钥继续base64对称加密返回
            return encoder.encode(encodebyte);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     *   解密
     * @param cryptograph 密文
     * @return
     */
    public static String decrypt(String cryptograph){
        Key privateKey = null;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            privateKey  =  (Key)inputStream.readObject();

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
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            BASE64Decoder decoder = new BASE64Decoder();
           byte[] bytes =  decoder.decodeBuffer(cryptograph);
            /** 解密后的处理 **/
           byte[] result =  cipher.doFinal(bytes);
           return new String(result);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
      return null;

    }
    public static void main(String[] args) throws Exception {
        String source = "恭喜发财!";// 要加密的字符串
        System.out.println("准备用公钥加密的字符串为：" + source);

        String cryptograph = encrypt(source);// 生成的密文
        System.out.print("用公钥加密后的结果为:" + cryptograph);
        System.out.println();

        String target = decrypt(cryptograph);// 解密密文
        System.out.println("用私钥解密后的字符串为：" + target);
        System.out.println();
    }

}
