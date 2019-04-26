package cn.com.flaginfo.rpc.common.aop;

import cn.com.flaginfo.exception.ErrorCode;

/**
 * @author: Meng.Liu
 * @date: 2018/12/12 上午9:53
 */
public enum RpcService {
    /**
     * mop
     */
    MOP_SERVICE(ErrorCode.MOP_SERVICE_UNAVAILABLE),
    /**
     * 用户中心
     */
    UCENTER_SERVICE(ErrorCode.USER_CENTER_SERVICE_UNAVAILABLE),
    /**
     * App消息服务
     */
    MESSAGE_SERVICE(ErrorCode.MESSAGE_SERVICE_UNAVAILABLE),
    /**
     * 通讯录服务
     */
    CONTACT_SERVICE(ErrorCode.CONTACT_SERVICE_UNAVAILABLE),
    /**
     * 平台消息中心
     */
    SMS_SERVICE(ErrorCode.SMS_SERVICE_UNAVAILABLE),
    /**
     * 内容推荐服务
     */
    ITEM_RECOMMEND_SERVICE(ErrorCode.ITEM_RECOMMEND_SERVICE),
    /**
     * 推荐交互服务
     */
    BIZ_COMMEND_SERVICE(ErrorCode.BIZ_COMMEND_SERVICE),
    /**
     * 未知服务
     */
    UNKNOWN_SERVICE(ErrorCode.UNKNOWN_SERVICE);

    /**
     * 服务错误码
     */
    private ErrorCode restfulCode;

    RpcService(ErrorCode restfulCode) {
        this.restfulCode = restfulCode;
    }

    public ErrorCode errorCode(){
        return this.restfulCode;
    }
}
