package cn.com.flaginfo.module.security.core;

import cn.com.flaginfo.module.security.exception.session.InvalidSessionException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 会话对象
 * @author: Meng.Liu
 * @date: 2019-05-09 14:03
 */
public interface Session extends Serializable {

    /**
     * 获取会话Id
     * @return
     */
    Serializable getId();

    /**
     * 设置session
     */
    void setId(Serializable sessionId);

    /**
     * 获取session绑定的ip地址
     * @return
     */
    String getHost();

    /**
     * 获取session开始时间
     * @return
     */
    Date getStartTimestamp();

    /**
     * 获取最后一次访问时间
     * @return
     */
    Date getLastAccessTimestamp();

    /**
     * 获取session结束时间
     * @return
     */
    Date getStopTimestamp();

    /**
     * 获取超时时长
     * @return
     */
    long getTimeout() throws InvalidSessionException;

    /**
     * 设置超时时长
     * @param maxIdleTimeInMillis
     * @throws InvalidSessionException
     */
    void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException;

    /**
     * 访问session
     * @throws InvalidSessionException
     */
    void touch() throws InvalidSessionException;

    /**
     * 停止session
     * @throws InvalidSessionException
     */
    void stop() throws InvalidSessionException;

    /**
     * 使失效
     */
    void expire();

    /**
     * 是否已经停止
     * @return
     */
    boolean isStopped();

    /**
     * 是否超时
     * @return
     */
    boolean isTimeout();

    /**
     * 是否有效
     * @return
     */
    boolean isValid();

    /**
     * 校验session
     * @throws InvalidSessionException
     */
    void validate() throws InvalidSessionException;

    /**
     * 获取session相关的所有缓存属性key
     * @return
     * @throws InvalidSessionException
     */
    Collection<Object> getAttributeKeys() throws InvalidSessionException;

    /**
     * 获取属性
     * @param attributeKeys
     * @return
     * @throws InvalidSessionException
     */
    Object getAttribute(Object attributeKeys) throws InvalidSessionException;

    /**
     * 添加属性
     * @param attributeKeys
     * @param value
     * @throws InvalidSessionException
     */
    void setAttribute(Object attributeKeys, Object value) throws InvalidSessionException;

    /**
     * 移除属性
     * @param attributeKeys
     * @return
     * @throws InvalidSessionException
     */
    Object removeAttribute(Object attributeKeys) throws InvalidSessionException;
}
