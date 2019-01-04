package cn.com.flaginfo.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/11/20 上午9:37
 */
@Configuration
@Slf4j
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    SpringContextUtils(){
        log.info("initializer spring context utils...");
    }

    public static <T> T getBean(Class<T> tClass){
        return getApplicationContext().getBean(tClass);
    }

    public static <T> T getBean(Class<T> tClass, Object... objects){
        return getApplicationContext().getBean(tClass, objects);
    }

    public static <T> T getBean(String name, Class<T> tClass){
        return getApplicationContext().getBean(name, tClass);
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static Object getBean(String name, Object... objects){
        return getApplicationContext().getBean(name, objects);
    }

    public static boolean containsBean(String name){
        return getApplicationContext().containsBean(name);
    }

    public static ApplicationContext getApplicationContext(){
        if( null == applicationContext ){
            throw new NullPointerException("spring application context did not init.");
        }
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
