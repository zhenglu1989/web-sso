package com.github.ebnew.ki4so.core.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.ebnew.ki4so.common.utils.StringUtils;
import com.github.ebnew.ki4so.core.dao.fs.KnightFileSystemDao;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenglu
 * @since 15/4/26
 */
public class KnightAppServiceImpl extends KnightFileSystemDao implements KnightAppService {

    private static final Logger logger = Logger.getLogger(KnightAppServiceImpl.class);

    //外部文件，优先级最高
    private static final String DEFAULT_EXTERNAL_FILE = "/Users/zhenglu/Documents/study/ki4so/ki4so-core/src/main/resources/apps.js";

    //默认的数据文件地址，在classpath下
    private static final String CLASS_PATH_FILE = "apps.js";

    //应用的映射表，appid为key，value为应用信息

    private Map<String,KnightApp> appMap = null;

    //服务本身应用信息
    private KnightApp knightApp = null;

    //在bean初始化init
    public void init(){
        this.classPathData = CLASS_PATH_FILE;
        this.externalData  = DEFAULT_EXTERNAL_FILE;
        loadAppData();
    }



    @Override
    protected void loadAppData() {
        try{
            if(appMap != null){
                appMap.clear();
            }
            appMap = null;
            String content = readDataFromFile();
            List<KnightApp> appList = JSON.parseObject(content, new TypeReference<List<KnightApp>>(){});
            appendSlashToHost(appList);
            if(appList != null && appList.size()  > 0 ){
                appMap = new HashMap<String, KnightApp>();
                for(KnightApp app : appList){
                    appMap.put(app.getAppId(),app);
                    if(knightApp == null){
                        if(app.isKnightService()){
                            this.knightApp = app;
                        }

                    }
                }
            }

        }catch (Exception e){
            logger.error("load app data error ::" + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 若主机host不以"/"结尾，则添加 "/" 否则不用处理
     * @param appList
     */
    private void appendSlashToHost(List<KnightApp> appList){
        for(KnightApp app : appList){
           if(app.getHost() != null && app.getHost().length() > 0 && !app.getHost().endsWith("/")){
               app.setHost(app.getHost() + "/");
           }
        }

    }

    @Override
    public KnightApp findAppById(String appId) {
        if(appMap != null){
            appMap.get(appId);
        }
        return null;
    }

    @Override
    public KnightApp findKi4soServerApp() {
        return this.knightApp;
    }

    @Override
    public KnightApp findAppByHost(String host) {
        if(StringUtils.isEmpty(host)){
            return null;
        }
        Collection<KnightApp> apps =  appMap.values();
        KnightApp knightApp1 =   findAppByUrl(apps,host);
         if(knightApp1 == null){
            knightApp1 =  findAppByUrl(apps,host+"/");
         }

         return knightApp1;
    }
    private KnightApp findAppByUrl(Collection<KnightApp> apps,String url){
        if(apps == null || apps.size() == 0 || StringUtils.isEmpty(url)){
            return null;
        }
        for(KnightApp app :apps){
            if(!StringUtils.isEmpty(url) && url.startsWith(app.getHost())){
                return app;
            }
        }
       return null;
    }
}
