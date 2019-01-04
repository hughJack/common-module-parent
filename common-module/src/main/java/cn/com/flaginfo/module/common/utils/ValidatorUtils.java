package cn.com.flaginfo.module.common.utils;

import java.util.regex.Pattern;

/**
 * @author: Meng.Liu
 * @date: 2018/12/13 下午4:48
 */
public class ValidatorUtils {

    /**
     * 手机号码格式
     */
    public static final String CHINA_MOBILE_REG = "^1[0-9]{10}$";
    /**
     * Email格式
     */
    public static final String EMAIL_REG = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 电话号码格式
     */
    public static final String PHONE_REG = "^[0]?\\d{2,3}[- ]?\\d{7,8}$";
    /**
     * 身份证格式
     */
    public static final String IDCARD_REG = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

    /**
     * 校验手机号格式
     * @return
     */
    public static boolean checkMobile(String mobile){
        return match(CHINA_MOBILE_REG, mobile);
    }

    /**
     * 校验电话号码格式
     * @return
     */
    public static boolean checkPhone(String phone){
        return match(PHONE_REG, phone);
    }

    /**
     * 校验邮箱格式
     * @return
     */
    public static boolean checkEmail(String email){
        return match(EMAIL_REG, email);
    }

    /**
     * 校验身份证格式
     * @return
     */
    public static boolean checkIdCard(String idCard){
        return match(IDCARD_REG, idCard);
    }

    /**
     * 正则校验
     * @param regex
     * @param input
     * @return
     */
    public static boolean match(String regex, String input){
       return Pattern.matches(regex, input);
    }

}
