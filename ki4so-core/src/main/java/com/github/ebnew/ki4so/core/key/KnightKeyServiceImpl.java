package com.github.ebnew.ki4so.core.key;

import com.github.ebnew.ki4so.core.dao.fs.KnightFileSystemDao;
import org.apache.log4j.Logger;
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
    private Map<String,KnightKey> keyMap = null;

    /**
     * 密钥映射表，key是appid，value是Key对象
     */
    private Map<String,KnightKey> appIdMap = null;

    @Override
    protected void loadAppData() {

    }

    @Override
    public KnightKey findKeyByKeyId(String keyId) {
        return null;
    }

    @Override
    public KnightKey findKeyByAppId(String appId) {
        return null;
    }

    @Override
    public boolean checkKeyFileExistByToken(String token) {
        return false;
    }

    @Override
    public Object generateKeyFile(String token) {
        return null;
    }
}
