package cn.com.flaginfo.rocketmq.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.aliyun.openservices.ons.api.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:40
 */
@Getter
@Setter
@ToString
@Slf4j
public class OnsMqMessage implements MqMessage {

	private Message mqMessageExt;

	public OnsMqMessage(Message messageExt) {
		this.mqMessageExt = messageExt;
		this.message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		this.msgId = messageExt.getMsgID();
		this.topic = messageExt.getTopic();
		this.tags = messageExt.getTag();
		this.keys = messageExt.getKey();
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
