package cn.com.flaginfo.rpc.common.service.impl;

import cn.com.flaginfo.rpc.common.configuration.RpcConfiguration;
import cn.com.flaginfo.rpc.common.domain.RpcLogInfo;
import cn.com.flaginfo.rpc.common.service.IRpcSlowlyHandle;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author: Meng.Liu
 * @date: 2019/4/12 上午11:24
 */
@ConditionalOnMissingBean(IRpcSlowlyHandle.class)
@Component
@Slf4j
public class DefaultRpcSlowlyHandler implements IRpcSlowlyHandle {
    /**
     * 成功
     */
    private static final String SUCCESS = "Success";
    /**
     * 失败
     */
    private static final String FAILED = "Failed";

    @Override
    public void callbackIfConfigureAsDetails(RpcLogInfo rpcLogInfo, RpcConfiguration configuration) {
        log.info("Rpc called : [{}] [{}] [{} {}({})] {} take {}ms",
                rpcLogInfo.getServiceName(),
                rpcLogInfo.getInterfaceName(),
                rpcLogInfo.getReturnType().getSimpleName(),
                rpcLogInfo.getMethodName(),
                rpcLogInfo.getParameterTypes(),
                rpcLogInfo.isSuccess() ? SUCCESS : FAILED,
                rpcLogInfo.getTakeTime());
        log.info("Rpc handle args : {}", JSONObject.toJSONString(rpcLogInfo.getArgs()));
        log.info("Rpc handle response : {}", JSONObject.toJSONString(rpcLogInfo.getResult()));
    }

    @Override
    public void callbackIfConfigureAsDetailsWhenSlowly(RpcLogInfo rpcLogInfo, RpcConfiguration configuration) {
        this.callbackIfConfigureAsDetails(rpcLogInfo, configuration);
    }

    @Override
    public void callbackIfConfigureAsSuccinct(RpcLogInfo rpcLogInfo, RpcConfiguration configuration) {
        log.info("Rpc called : [{}] [{}] [{}] {} take {}ms",
                rpcLogInfo.getServiceName(),
                rpcLogInfo.getInterfaceName(),
                rpcLogInfo.getMethodName(),
                rpcLogInfo.isSuccess() ? SUCCESS : FAILED,
                rpcLogInfo.getTakeTime());
    }

    @Override
    public void callbackIfConfigureAsSuccinctWhenSlowly(RpcLogInfo rpcLogInfo, RpcConfiguration configuration) {
        this.callbackIfConfigureAsSuccinct(rpcLogInfo, configuration);
    }
}
