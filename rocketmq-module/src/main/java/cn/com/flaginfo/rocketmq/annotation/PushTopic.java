package cn.com.flaginfo.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PushTopic {

    /**
     * 所属组
     *
     * @return
     */
    String group() default "";

    /**
     * 订购的主题
     *
     * @return
     */
    String title();

    /**
     * 订购的tag 可以为空
     *
     * @return
     */
    String tag() default "*";

    /**
     * retry限制, 不配置或-1表示不限制直到过期不消费， 见MessageListenerAdapter
     *
     * @return
     */
    int retryLimit() default -1;


}
