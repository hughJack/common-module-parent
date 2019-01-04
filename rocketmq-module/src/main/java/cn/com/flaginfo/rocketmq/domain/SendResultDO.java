package cn.com.flaginfo.rocketmq.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 下午1:33
 */
@Data
public class SendResultDO implements Serializable {

    private Boolean success;

    private String messageId;

}
