package cn.com.flaginfo.module.common.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 下午5:49
 */
public class GenderUtils {

    public enum Gender{
        /**
         * 男士
         */
        Male(Arrays.asList(1), "M", "男"),
        /**
         * 女士
         */
        Female(Arrays.asList(0, 2), "F", "女"),
        /**
         * 其他
         */
        Other(Arrays.asList(), "S", "未知");

        List<Integer> codes;
        String charStr;
        String chinese;
        Gender(List<Integer> codes, String charStr, String chinese){
            this.codes = codes;
            this.charStr = charStr;
            this.chinese = chinese;
        }

        public List<Integer> codes(){
            return this.codes;
        }
        public String charStr(){
            return this.charStr;
        }
        public String chinese(){
            return this.chinese;
        }
    }

    /**
     * 根据性别code来获取性别枚举值
     * @param code
     * @return
     */
    public static Gender getGenerWithCode(Integer code){
        if( null == code ){
            return Gender.Other;
        }
        for (Gender value : Gender.values()) {
            if( value.codes().contains(code) ){
                return value;
            }
        }
        return Gender.Other;
    }

    /**
     * 性别的英文
     *
     * @param sex
     * @return
     */
    public static String getGenderForChar(Integer sex) {
        return getGenerWithCode(sex).charStr();
    }

    /**
     * 性别的中文
     * @param sex
     * @return
     */
    public static String getGenderForChinese(Integer sex) {
        return getGenerWithCode(sex).chinese();
    }
}
