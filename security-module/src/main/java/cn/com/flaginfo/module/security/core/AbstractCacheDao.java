package cn.com.flaginfo.module.security.core;

import cn.com.flaginfo.module.security.core.support.EmptyAccount;
import cn.com.flaginfo.module.security.core.support.UuidSessionIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2019-05-09 15:37
 */
@Slf4j
public abstract class AbstractCacheDao implements CacheDao {

    private SessionIdGenerator sessionIdGenerator;

    private static final Account EMPTY = new EmptyAccount();

    public AbstractCacheDao() {
        this.sessionIdGenerator = new UuidSessionIdGenerator();
    }

    public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
        this.sessionIdGenerator = sessionIdGenerator;
    }

    public SessionIdGenerator getSessionIdGenerator() {
        return sessionIdGenerator;
    }

    @Override
    public Serializable createSession(Session session) {
        Serializable sessionId = this.doCreateSession(session);
        verifySessionId(sessionId);
        session.setId(sessionId);
        return this.cacheSession(sessionId, session);
    }

    /**
     * 校验session是否正确
     * @param sessionId
     */
    private void verifySessionId(Serializable sessionId) {
        if (sessionId == null) {
            String msg = "sessionId returned from doCreate implementation is null.  Please verify the implementation.";
            throw new IllegalStateException(msg);
        }
    }
    
    /**
     * 生成sessionId
     * @return
     */
    protected Serializable generateSessionId() {
        if (this.sessionIdGenerator == null) {
            String msg = "sessionIdGenerator attribute has not been configured.";
            throw new IllegalStateException(msg);
        }
        return this.sessionIdGenerator.generator();
    }


    @Override
    public void updateSession(Session session) {
        this.cacheSession(session.getId(), session);
    }

    /**
     * 删除session的同时删除session account
     *
     * @param session
     */
    @Override
    public void deleteSession(Session session) {
        this.validateSession(session);
        this.deleteSessionAccount(session);
        this.doDeleteSession(session);
    }

    /**
     * 创建session Id
     * @param session
     * @return
     */
    protected abstract Serializable doCreateSession(Session session);

    /**
     * 缓存session
     * @param id
     * @param session
     * @return
     */
    protected abstract Session cacheSession(Serializable id, Session session);

    /**
     * 删除session的业务逻辑
     *
     * @param session
     */
    protected abstract void doDeleteSession(Session session);

    @Override
    public Account cacheSessionAccount(Session session, Account account) {
        this.validateSession(session);
        return this.doCacheSessionAccount(session, account);
    }

    @Override
    public void updateSessionAccount(Session session, Account account) {
        this.validateSession(session);
        this.cacheSessionAccount(session, account);
    }

    @Override
    public Account readSessionAccount(Session session) {
        this.validateSession(session);
        Account account = this.doReadSessionAccount(session);
        if( null == account ){
            return EMPTY;
        } else {
            return account;
        }
    }

    @Override
    public void deleteSessionAccount(Session session) {
        this.validateSession(session);
        this.doDeleteSessionAccount(session);
    }

    /**
     * 缓存session Account
     * @param session
     * @param account
     * @return
     */
    protected abstract Account doCacheSessionAccount(Session session, Account account);

    /**
     * 读取session account
     * @param session
     * @return
     */
    protected abstract Account doReadSessionAccount(Session session);

    /**
     * 删除session
     * @param session
     */
    protected abstract void doDeleteSessionAccount(Session session);


    private void validateSession(Session session){
        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
    }
}
