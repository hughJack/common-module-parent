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
     * 服务名称
     * @return
     */
    RpcService service();
    /**
     * 接口
     * @return
     */
    Class<?> serviceInterface();
}
