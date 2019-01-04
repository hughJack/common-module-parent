package cn.com.flaginfo.module.common.utils;

import java.util.UUID;

/**
 * @author: Meng.Liu
 * @date: 2018/11/14 上午10:56
 */
public class TraceLogUtils {

    public static String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
