package cn.com.flaginfo.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/29 15:53
 */
@Slf4j
public class Md5Utils {

    public static String encode(String str){
        if(StringUtils.isBlank(str)){
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] md5Bytes = md5.digest();
            return Base64.encodeBase64String(md5Bytes);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

}
