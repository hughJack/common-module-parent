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
    public void handle(RpcLogInfo rpcLogInfo, RpcConfiguration rpcConfiguration) {
        if (null == rpcConfiguration
                || rpcConfiguration.getLogLevel() == RpcConfiguration.RpcLogLevel.Off
                || null == rpcLogInfo) {
            return;
        }
        switch (rpcConfiguration.getLogLevel()) {
            case Detailed:
                this.logDetailed(rpcLogInfo);
                break;
            case Detailed_When_Slow:
                if (rpcLogInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
                    this.logDetailed(rpcLogInfo);
                }
                break;
            case Succinct_When_Slow:
                if (rpcLogInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
                    this.logSuccinct(rpcLogInfo);
                }
                break;
            case Succinct:
            default:
                this.logSuccinct(rpcLogInfo);
                break;
        }
        if (rpcLogInfo.getTakeTime() > rpcConfiguration.getSlowThreshold()) {
            log.warn("Rpc handle rpc is too slow, take {}ms.", rpcLogInfo.getTakeTime());
        }
    }


    private void logSuccinct(RpcLogInfo rpcLogInfo) {
        log.info("Rpc called : [{}] [{}] [{}] {} take {}ms",
                rpcLogInfo.getServiceName(),
                rpcLogInfo.getInterfaceName(),
                rpcLogInfo.getMethodName(),
                rpcLogInfo.isSuccess() ? SUCCESS : FAILED,
                rpcLogInfo.getTakeTime());
    }

    private void logDetailed(RpcLogInfo rpcLogInfo) {
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

}
