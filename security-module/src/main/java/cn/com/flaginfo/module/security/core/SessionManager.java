package cn.com.flaginfo.module.security.core;

import cn.com.flaginfo.module.security.exception.session.InvalidSessionException;

import java.util.Collection;
import java.util.Date;

/**
 * Session会话管理器
 * @author: Meng.Liu
 * @date: 2019-05-09 14:07
 */
public interface SessionManager {

    /**
     * 根据session key获取session
     * @param sessionKey
     * @return
     * @throws InvalidSessionException
     */
    Session getSession(SessionKey sessionKey) throws InvalidSessionException ;


    /**
     * 获取回话绑定用户的信息
     * @param session
     * @return
     * @throws InvalidSessionException
     */
    Account getAccount(Session session) throws InvalidSessionException;

    /**
     * session是否有效
     * @param sessionKey
     * @return
     */
    boolean isValid(SessionKey sessionKey);

    /**
     * 校验session
     * @param sessionKey
     * @return
     */
    void checkValid(SessionKey sessionKey) throws InvalidSessionException;

    /**
     * 访问session
     * @param sessionKey
     */
    void touch(SessionKey sessionKey) throws InvalidSessionException;

    /**
     * 获取session的访问地址
     * @param sessionKey
     * @return
     */
    String getHost(SessionKey sessionKey);

    /**
     * 停止session
     * @param sessionKey
     */
    void stop(SessionKey sessionKey) throws InvalidSessionException;

    /**
     * 获取开始时间
     * @param sessionKey
     * @return
     */
    Date getStartTimestamp(SessionKey sessionKey);

    /**
     * 获取最后一次访问的时间
     * @param sessionKey
     * @return
     */
    Date getLastAccessTimestamp(SessionKey sessionKey);

    /**
     * 设置session的超时时间
     * @param sessionKey
     * @param maxIdleTimeInMillis
     */
    void setTimeout(SessionKey sessionKey, long maxIdleTimeInMillis) throws InvalidSessionException;

    /**
     * 获取超时时间
     * @param sessionKey
     */
    long getTimeout(SessionKey sessionKey) throws InvalidSessionException;

    /**
     * 获取session相关的所有缓存属性key
     * @param sessionKey
     * @return
     * @throws InvalidSessionException
     */
    Collection<Object> getAttributeKeys(SessionKey sessionKey) throws InvalidSessionException;

    /**
     * 获取属性
     * @param sessionKey
     * @param attributeKeys
     * @return
     * @throws InvalidSessionException
     */
    Object getAttribute(SessionKey sessionKey, Object attributeKeys) throws InvalidSessionException;

    /**
     * 添加属性
     * @param sessionKey
     * @param attributeKeys
     * @param value
     * @throws InvalidSessionException
     */
    void setAttribute(SessionKey sessionKey, Object attributeKeys, Object value) throws InvalidSessionException;

    /**
     * 移除属性
     * @param sessionKey
     * @param attributeKeys
     * @return
     * @throws InvalidSessionException
     */
    Object removeAttribute(SessionKey sessionKey, Object attributeKeys) throws InvalidSessionException;
}
