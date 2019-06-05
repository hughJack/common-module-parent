package cn.com.flaginfo.module.security.core;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: Meng.Liu
 * @date: 2019-05-09 15:24
 */
public interface CacheDao {

    /**
     * 缓存session
     * @return
     */
    Serializable createSession(Session session);

    /**
     * 获取session
     * @param sessionKey
     * @return
     */
    Session readSession(SessionKey sessionKey);

    /**
     * 更新session
     * @param session
     */
    void updateSession(Session session);

    /**
     * 删除session
     * @param session
     */
    void deleteSession(Session session);

    /**
     * 获取所有的session
     * @return
     */
    Collection<Session> getAllSessions();

    /**
     * 缓存session对应的账户信息
     */
    Account cacheSessionAccount(Session session, Account account);

    /**
     * 获取session对应的账户信息
     * @param session
     * @return
     */
    Account readSessionAccount(Session session);

    /**
     * 更新session对应的账户信息
     * @param session
     * @param account
     */
    void updateSessionAccount(Session session, Account account);

    /**
     * 删除session对应的账户信息
     * @param session
     */
    void deleteSessionAccount(Session session);
}
