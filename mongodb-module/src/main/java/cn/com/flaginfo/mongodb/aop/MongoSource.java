package cn.com.flaginfo.mongodb.aop;

import java.lang.annotation.*;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/12 11:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface MongoSource {

    String value() default "";

}
