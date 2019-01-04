package cn.com.flaginfo.module.common.utils;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 下午5:49
 */
public class GenderUtils {

    public enum Gender{
        /**
         * 男士
         */
        Male,
        /**
         * 女士
         */
        Female,
        /**
         * 其他
         */
        Other
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
        String gender;
        switch (code) {
            case 1:
                return Gender.Male;
            case 0:
            case 2:
                return Gender.Female;
            default:
                return Gender.Other;
        }
    }
}
