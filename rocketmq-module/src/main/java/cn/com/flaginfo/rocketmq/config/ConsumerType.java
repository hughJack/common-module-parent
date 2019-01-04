package cn.com.flaginfo.rocketmq.config;

/**
 * @author: Meng.Liu
 * @date: 2018/11/23 上午11:52
 */
public enum ConsumerType {
    /**
     * 服务器推送模式
     */
    Push,
    /**
     * 客户端拉去模式
     */
    Pull,
    /**
     * 兼容模式
     */
    Compatible
}
