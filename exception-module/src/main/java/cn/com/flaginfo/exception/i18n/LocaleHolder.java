package cn.com.flaginfo.exception.i18n;

import java.util.Locale;

/**
 * @author: Meng.Liu
 * @date: 2018/12/17 下午3:21
 */
public class LocaleHolder {

    private static final ThreadLocal<Locale> HOLDER = new ThreadLocal<>();

    public static Locale get(){
        return HOLDER.get();
    }

    public static void set(Locale locale){
        HOLDER.set(locale);
    }

    public static void set(String language){
        if( null == language ){
            return;
        }
        HOLDER.set(Locale.forLanguageTag(language));
    }

    public static void clear(){
        HOLDER.remove();
    }
}
