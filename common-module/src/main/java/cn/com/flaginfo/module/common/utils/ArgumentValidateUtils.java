package cn.com.flaginfo.module.common.utils;


import cn.com.flaginfo.exception.common.ArgumentException;

import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * @author: Meng.Liu
 * @date: 2018/11/28 上午10:36
 */
public class ArgumentValidateUtils {

    private static final String DEFAULT_NULL_MSG = "请求参数为空";

    private static final String DEFAULT_ERR_MSG = "请求参数{0}不正确";

    public static void argumentRequire(Object obj, String args) throws ArgumentException {
        argumentRequire(obj, args, DEFAULT_ERR_MSG);
    }

    public static void argumentRequire(Object obj, String[] args) throws ArgumentException {
        argumentRequire(obj, args, DEFAULT_ERR_MSG);
    }

    public static void argumentRequire(Object obj, String args, String errmsg) throws ArgumentException {
        argumentRequire(obj, new String[]{args}, errmsg);
    }

    public static void argumentRequire(Object obj, String[] args, String errmsg) throws ArgumentException {
        if( null == obj){
            throw new ArgumentException(DEFAULT_NULL_MSG);
        }
        if( null == args || args.length == 0 ){
            return;
        }
        String fieldName = "";
        try {
            Field field;
            Object value;
            for(String s : args ){
                fieldName = s;
                field = obj.getClass().getDeclaredField(s);
                field.setAccessible(true);
                value = field.get(obj);
                if( null == value){
                    throw new ArgumentException(MessageFormat.format(errmsg, s));
                }
            }
        } catch (NoSuchFieldException e) {
            throw new ArgumentException("Cannot find this field ["+ fieldName +"] from class [" + obj.getClass().getSimpleName() + "]");
        } catch (IllegalAccessException e) {
            throw new ArgumentException("Cannot check this field ["+ fieldName +"] value from class [" + obj.getClass().getSimpleName() + "], Illegal Access");
        }
    }
}
