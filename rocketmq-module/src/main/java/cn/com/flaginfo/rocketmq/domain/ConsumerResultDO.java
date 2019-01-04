package cn.com.flaginfo.rocketmq.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2018/11/23 下午3:12
 */
@Data
public class ConsumerResultDO implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success = true;

    /**
     * 是否需要重新消费
     */
    private Boolean retry = false;

    /**
     * 结果描述
     */
    private String message;

}
