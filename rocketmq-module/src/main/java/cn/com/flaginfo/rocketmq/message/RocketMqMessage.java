package cn.com.flaginfo.rocketmq.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;


/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:40
 */
@Setter
@Getter
@ToString
@Slf4j
public class RocketMqMessage implements MqMessage {

	private MessageExt messageExt;

	public RocketMqMessage(MessageExt messageExt) {
		this.messageExt = messageExt;
		this.message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		this.msgId = messageExt.getMsgId();
		this.topic = messageExt.getTopic();
		this.tags = messageExt.getTags();
		this.keys = messageExt.getKeys();
		this.bornTimestamp = messageExt.getBornTimestamp();
		this.reconsumeTimes = messageExt.getReconsumeTimes();
		this.retryTimes = messageExt.getReconsumeTimes();
	}

	/**
	 * 消息的keys
	 */
	private String keys;

	/**
	 * 消息的topic
	 */
	private String topic;

	/**
	 * 消息的tags
	 */
	private String tags;

	/**
	 * 消息ID
	 */
	private String msgId;

	/**
	 * 消息内容
	 */
	private String message;

	/**
	 * 消息产生时间
	 */
	private Long bornTimestamp;

	/**
	 * 消息消费次数
	 */
	private Integer reconsumeTimes;

	/**
	 * retry次数
	 */
	private int retryTimes;
}
