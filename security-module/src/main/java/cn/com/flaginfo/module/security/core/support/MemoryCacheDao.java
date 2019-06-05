package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.AbstractCacheDao;
import cn.com.flaginfo.module.security.core.Account;
import cn.com.flaginfo.module.security.core.Session;
import cn.com.flaginfo.module.security.core.SessionKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 内存的缓存处理器
 * @author: Meng.Liu
 * @date: 2019-05-09 15:35
 */
@Slf4j
public class MemoryCacheDao extends AbstractCacheDao {

    private ConcurrentMap<Serializable, Session> sessions;

    private ConcurrentMap<Serializable, Account> accounts;

    public MemoryCacheDao() {
        this.sessions = new ConcurrentHashMap<>();
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    protected Serializable doCreateSession(Session session) {
       return this.generateSessionId();
    }

    @Override
    protected Session cacheSession(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        return this.sessions.putIfAbsent(id, session);
    }

    @Override
    protected void doDeleteSession(Session session) {
        Serializable id = session.getId();
        if (id != null) {
            this.sessions.remove(id);
        }
    }

    @Override
    public Session readSession(SessionKey sessionKey) {
        return this.sessions.get(sessionKey.getSessionId());
    }


    @Override
    protected Account doCacheSessionAccount(Session session, Account account) {
        return this.accounts.putIfAbsent(session.getId(), account);
    }

    @Override
    protected Account doReadSessionAccount(Session session) {
        return this.accounts.get(session.getId());
    }

    @Override
    protected void doDeleteSessionAccount(Session session) {
        this.accounts.remove(session.getId());
    }

    @Override
    public Collection<Session> getAllSessions() {
        Collection<Session> values = sessions.values();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableCollection(values);
        }
    }

}
