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
     * 当设置为详细日志时的回调
     * @param rpcLogInfo
     * @param configuration
     */
    void callbackIfConfigureAsDetails(RpcLogInfo rpcLogInfo, RpcConfiguration configuration);

    /**
     * 当设置为慢查询记录详细日志时的回调
     * @param rpcLogInfo
     * @param configuration
     */
    void callbackIfConfigureAsDetailsWhenSlowly(RpcLogInfo rpcLogInfo, RpcConfiguration configuration);

    /**
     * 当设置为简洁日志时的回调
     * @param rpcLogInfo
     * @param configuration
     */
    void callbackIfConfigureAsSuccinct(RpcLogInfo rpcLogInfo, RpcConfiguration configuration);

    /**
     * 当设置为慢查询时记录简洁日志时的回调
     * @param rpcLogInfo
     * @param configuration
     */
    void callbackIfConfigureAsSuccinctWhenSlowly(RpcLogInfo rpcLogInfo, RpcConfiguration configuration);
}
