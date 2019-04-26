package cn.com.flaginfo.rpc.common.aop;

import java.lang.annotation.*;

/**
 * RPC接口注解，用于标注RPC接口封装了
 * AOP会扫描该注解并记录调用日志
 * @author: Meng.Liu
 * @date: 2018/12/11 上午11:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RpcInterface {
    /**
     * 服务
     * @return
     */
    RpcService service() default RpcService.UNKNOWN_SERVICE;
    /**
     * 接口
     * @return
     */
    Class<?> serviceInterface();
    /**
     * 服务名称
     * @return
     */
    String serviceName() default "";
    /**
     * 服务编码
     * @return
     */
    long serviceCode() default Long.MIN_VALUE;
    /**
     * 服务错误信息
     * @return
     */
    String errorMessage() default "";
}
