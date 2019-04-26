package cn.com.flaginfo.module.common.annotation;

import java.lang.annotation.*;

/**
 * @author: Meng.Liu
 * @date: 2019/4/17 下午5:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidParams {
}
