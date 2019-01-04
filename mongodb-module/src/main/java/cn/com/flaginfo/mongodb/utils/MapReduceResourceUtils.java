package cn.com.flaginfo.mongodb.utils;

import cn.com.flaginfo.module.common.utils.SpringContextUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Meng.Liu
 * @date: 2018/12/29 下午2:32
 */
public class MapReduceResourceUtils {

    private static ResourceLoader resourceLoader;

    private static Pattern PATTERN = Pattern.compile("(\\{([\\d+])\\})");

    private static ResourceLoader getResourceLoader(){
        if( null == resourceLoader ){
            resourceLoader = SpringContextUtils.getApplicationContext();
        }
        return resourceLoader;
    }

    /**
     * 获取js文件
     * @param path
     * @return
     */
    public static String getJsFunction(String path){
        if (ResourceUtils.isUrl(path)) {
            Resource functionResource = getResourceLoader().getResource(path);
            if (!functionResource.exists()) {
                throw new InvalidDataAccessApiUsageException(String.format("Resource %s not found!", path));
            }
            Scanner scanner = null;
            try {
                scanner = new Scanner(functionResource.getInputStream());
                return scanner.useDelimiter("\\A").next();
            } catch (IOException e) {
                throw new InvalidDataAccessApiUsageException(String.format("Cannot read map-reduce file %s!", path), e);
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
        return path;
    }

    /**
     * 获取js文件，并替换
     * @param path
     * @param args
     * @return
     */
    public static String getJsFunctionAndReplaceArgs(String path, Object... args){
        String func = getJsFunction(path);
        if (null != args && args.length != 0) {
            Matcher matcher = PATTERN.matcher(func);
            while (matcher.find()){
                int index = Integer.valueOf(matcher.group(2));
                if(index < args.length){
                    func = func.replaceAll("\\{" + index + "\\}", String.valueOf(args[index]));
                }
            }
        }
        return func;
    }
}
