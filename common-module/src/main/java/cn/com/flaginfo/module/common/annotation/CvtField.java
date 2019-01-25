package cn.com.flaginfo.module.common.annotation;

import java.lang.annotation.*;

/**
 * 字段转换器注解
 * @author: Meng.Liu
 * @date: 2019/1/24 下午1:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CvtField {

    /**
     * 源字段名称
     * @return
     */
    String[] sourceField();

    /**
     * 自定义转换器
     * @return
     */
    boolean customCvt() default false;

    /**
     * 转换方法的类名称
     * @return
     */
    Class<? extends CvtTransformHandler> transformClass() default CvtTransformHandler.class;

    /**
     * 转换方法的全称
     * @return
     */
    String transformClassName() default "";
}
