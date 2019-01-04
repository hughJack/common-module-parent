package cn.com.flaginfo.rocketmq.utils;

import cn.com.flaginfo.rocketmq.domain.ConsumerResultDO;

/**
 * @author: Meng.Liu
 * @date: 2018/11/26 下午3:04
 */
public class MqResultUtils {

    /**
     * 消费成功
     * @return
     */
    public static ConsumerResultDO consumeSuccess(){
        return consumeSuccess(null);
    }

    /**
     * 消费成功
     * @param message
     * @return
     */
    public static ConsumerResultDO consumeSuccess(String message){
        ConsumerResultDO resultDO = new ConsumerResultDO();
        resultDO.setSuccess(true);
        resultDO.setMessage(message);
        return resultDO;
    }

    /**
     * 重试
     * @return
     */
    public static ConsumerResultDO consumeAgain(){
        return consumeAgain(null);
    }

    /**
     * 重试
     * @param message
     * @return
     */
    public static ConsumerResultDO consumeAgain(String message){
        ConsumerResultDO resultDO = new ConsumerResultDO();
        resultDO.setSuccess(false);
        resultDO.setRetry(true);
        resultDO.setMessage(message);
        return resultDO;
    }
}
