package cn.com.flaginfo.rpc.common.utils;


import cn.com.flaginfo.exception.rpc.RpcArgumentsException;
import cn.com.flaginfo.exception.rpc.RpcException;
import cn.com.flaginfo.exception.rpc.RpcNullException;

import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * @author: Meng.Liu
 * @date: 2018/11/28 上午10:36
 */
public class RpcArgumentValidateUtils {

    private static final String DEFAULT_NULL_MSG = "RPC调用失败，请求参数为空";

    private static final String DEFAULT_ERR_MSG = "RPC调用失败，请求参数{0}不正确";

    public static void rpcArgumentRequire(Object obj, String args) throws RpcException {
        rpcArgumentRequire(obj, args, DEFAULT_ERR_MSG);
    }

    public static void rpcArgumentRequire(Object obj, String[] args) throws RpcException {
        rpcArgumentRequire(obj, args, DEFAULT_ERR_MSG);
    }

    public static void rpcArgumentRequire(Object obj, String args, String errmsg) throws RpcException {
        rpcArgumentRequire(obj, new String[]{args}, errmsg);
    }

    public static void rpcArgumentRequire(Object obj, String[] args, String errmsg) throws RpcException {
        if( null == obj){
            throw new RpcNullException(DEFAULT_NULL_MSG);
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
                    throw new RpcArgumentsException(MessageFormat.format(errmsg, s));
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RpcException("Cannot find this field ["+ fieldName +"] from class [" + obj.getClass().getSimpleName() + "]");
        } catch (IllegalAccessException e) {
            throw new RpcException("Cannot check this field ["+ fieldName +"] value from class [" + obj.getClass().getSimpleName() + "], Illegal Access");
        }
    }
}
