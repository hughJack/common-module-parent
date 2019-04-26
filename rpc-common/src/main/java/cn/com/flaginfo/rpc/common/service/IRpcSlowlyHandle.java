package cn.com.flaginfo.rpc.common.service;

import cn.com.flaginfo.rpc.common.configuration.RpcConfiguration;
import cn.com.flaginfo.rpc.common.domain.RpcLogInfo;

/**
 * RPC调用缓慢执行类
 * @author: Meng.Liu
 * @date: 2019/4/12 上午11:19
 */
public interface IRpcSlowlyHandle {

    /**
     * 回调执行器，当日志关闭时，rpcLogInfo为空
     * @param rpcLogInfo
     * @param rpcConfiguration
     */
    void handle(RpcLogInfo rpcLogInfo, RpcConfiguration rpcConfiguration);
}
