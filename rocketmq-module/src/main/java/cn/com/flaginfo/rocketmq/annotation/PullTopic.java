package cn.com.flaginfo.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午10:12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PullTopic {

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
     * 标签
     * @return
     */
    String tag() default "*";

    /**
     * 定时拉取的线程数，默认1个
     *
     * @return
     */
    int threadNum() default 1;

    /**
     * 定时拉取，启动后执行等待时间 默认1秒
     *
     * @return
     */
    int initialDelay() default 60;// 1s

    /**
     * 定时拉取，执行一次后等待时间，默认60s
     *
     * @return
     */
    int delay() default 60; // 60s

    /**
     * 定时拉取消息池中的最大数据数量
     *
     * @return
     */
    int maxNum() default 1000;
}
