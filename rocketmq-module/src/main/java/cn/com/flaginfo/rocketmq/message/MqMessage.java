package cn.com.flaginfo.rocketmq.message;

/**
 * @author: Meng.Liu
 * @date: 2018/11/22 上午11:40
 */
public interface MqMessage {

	/**
	 *
	 * @return
	 */
	Long getBornTimestamp();

	/**
	 *
	 * @return
	 */
	Integer getReconsumeTimes();

	/**
	 *
	 * @return
	 */
	String getKeys();

	/**
	 *
	 * @return
	 */
	String getTopic();

	/**
	 *
	 * @return
	 */
	String getTags();

	/**
	 *
	 * @return
	 */
	String getMessage();

	/**
	 *
	 * @return
	 */
	String getMsgId();

}