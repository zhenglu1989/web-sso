package com.github.ebnew.ki4so.common;

import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightBase64Coder {

    private static final Logger logger = Logger.getLogger(KnightBase64Coder.class);

    /**
     * 编码
     * @param bstr
     * @return
     */

    public static String encryptBase64(byte[] bstr){
        if(bstr == null || bstr.length == 0){
            return null;
        }
        return new  BASE64Encoder().encode(bstr);
    }

    /**
     * 解码
     */
    public static byte[] decryptBase64(String str){
        if(str == null || str.length() == 0){
            return null;
        }
        byte[] result = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            result =   decoder.decodeBuffer(str);


        } catch (IOException e) {
            logger.error("出错:decode base64 error:: " + e.getMessage() );
        }

        return result;
    }
}


