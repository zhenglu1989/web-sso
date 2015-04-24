package com.github.ebnew.ki4so.core.dao.fs;

import com.github.ebnew.ki4so.common.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author zhenglu
 * @since 15/4/24
 */
public abstract class KnightFileSystemDao {

    private static final Logger logger = Logger.getLogger(KnightFileSystemDao.class);
    /**
     * 外部数据文件地址，优先级更高
     */
    protected String externalData = null;

    /**
     * 默认的数据文件地址,在classpath下
     */

    protected String classPathData = null;

    public String getClassPathData() {
        return classPathData;
    }

    public String getExternalData() {
        return externalData;
    }

    /**
     * 重新设置外部数据路径
     * 设置成功后，需要触发重新假装数据内容
     * @param externalData
     */
    public void setExternalData(String externalData){
       if(externalData != null && externalData.length() > 0 ){
           //外部数据文件路径不同，需要重新加载
           if(!externalData.equals(this.externalData)){
               this.externalData = externalData;

               loadAppData();
           }
       }else {
           if(this.externalData != null && this.externalData.length() > 0 ){
               this.loadAppData();
           }
       }
    }

    /**
     * 从数据文件中加载数据，抽象方法，具体子类实现逻辑
     */
    protected abstract void loadAppData();

    public String readDataFromFile(){

        try{
            InputStream inputStream = null;
            //优先使用外部数据文件
            if(!StringUtils.isEmpty(this.getExternalData())){
                inputStream = new FileInputStream(this.getExternalData());
            }
            if(inputStream == null){
                if(!StringUtils.isEmpty(this.getClassPathData())){
                    Resource resource  = new ClassPathResource(this.getClassPathData());
                    inputStream = resource.getInputStream();
                }
            }
            if(inputStream != null){
                return new String(readStream(inputStream));
            }


        }catch (Exception e){
            logger.error("readDataFromFile has error :"  + e.getMessage());
        }

        return null;


    }

    public static byte[] readStream(InputStream inputStream){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = inputStream.read(buffer)) != -1){
               outputStream.write(buffer,0,len);
            }
            outputStream.close();
            inputStream.close();
            return outputStream.toByteArray();

        }catch (Exception e){
         logger.error("readStream has error::" +e.getMessage());
        }
        return null;
    }


}
