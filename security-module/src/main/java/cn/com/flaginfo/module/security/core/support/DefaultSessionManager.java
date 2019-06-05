package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.*;
import cn.com.flaginfo.module.security.exception.session.ExpiredSessionException;
import cn.com.flaginfo.module.security.exception.session.InvalidSessionException;
import cn.com.flaginfo.module.security.exception.session.UnknownSessionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: Meng.Liu
 * @date: 2019-05-09 14:13
 */
@Slf4j
public class DefaultSessionManager implements SessionManager {

    /**
     * 默认session超时时长，单位:毫秒
     */
    public static final long DEFAULT_SESSION_TIMEOUT = TimeUnit.MINUTES.toMillis(30);

    /**
     * 缓存工具
     */
    private CacheDao cacheDao;

    /**
     * 是否删除无效session
     */
    private boolean deleteInvalidSessions;

    /**
     * session过期扫描器
     */
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * 扫描器是否运行
     */
    private boolean sessionValidateScheduledIsRun;

    public DefaultSessionManager() {
        this.deleteInvalidSessions = true;
        sessionValidateScheduledIsRun = false;
        this.cacheDao = new MemoryCacheDao();
    }

    public CacheDao getCacheDao() {
        return cacheDao;
    }

    public void setCacheDao(CacheDao cacheDao) {
        this.cacheDao = cacheDao;
    }

    public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
        this.deleteInvalidSessions = deleteInvalidSessions;
    }

    public boolean isDeleteInvalidSessions() {
        return deleteInvalidSessions;
    }

    @Override
    public Session getSession(SessionKey sessionKey) throws InvalidSessionException  {
        if (null == sessionKey) {
            throw new NullPointerException("SessionKey cannot be null");
        }
        Session session = this.doGetSession(sessionKey);
        this.doValidateSession(session, sessionKey);
        return session;
    }

    @Override
    public Account getAccount(Session session) throws InvalidSessionException {
        return null;
    }

    private void doValidateSession(Session session, SessionKey sessionKey) throws InvalidSessionException {
        if (null == session) {
            throw new UnknownSessionException("cannot find validateSession with sessionKey [" + sessionKey + "]");
        }
        try {
            session.validate();
        } catch (InvalidSessionException ise) {
            this.onInvalidationSession(session, ise, sessionKey);
            throw ise;
        }
    }


    @Override
    public boolean isValid(SessionKey sessionKey) {
        try {
            this.checkValid(sessionKey);
            return true;
        } catch (InvalidSessionException e) {
            return false;
        }
    }

    @Override
    public void checkValid(SessionKey sessionKey) throws InvalidSessionException {
        this.getSession(sessionKey);
    }

    @Override
    public void touch(SessionKey sessionKey) throws InvalidSessionException {
        Session session = this.getSession(sessionKey);
        session.touch();
        this.onSessionChange(session);
    }

    @Override
    public String getHost(SessionKey sessionKey) {
        return this.getSession(sessionKey).getHost();
    }

    @Override
    public void stop(SessionKey sessionKey) throws InvalidSessionException {
        Session session = this.getSession(sessionKey);
        try {
            session.stop();
            this.onSessionStop(session);
        } finally {
            this.afterSessionStop(session);
        }
    }

    @Override
    public Date getStartTimestamp(SessionKey sessionKey) {
        return this.getSession(sessionKey).getStartTimestamp();
    }

    @Override
    public Date getLastAccessTimestamp(SessionKey sessionKey) {
        return this.getSession(sessionKey).getLastAccessTimestamp();
    }

    @Override
    public void setTimeout(SessionKey sessionKey, long maxIdleTimeInMillis) throws InvalidSessionException {
        Session session = this.getSession(sessionKey);
        session.setTimeout(maxIdleTimeInMillis);
        this.onSessionChange(session);
    }

    @Override
    public long getTimeout(SessionKey sessionKey) throws InvalidSessionException {
        return this.getSession(sessionKey).getTimeout();
    }

    @Override
    public Collection<Object> getAttributeKeys(SessionKey sessionKey) throws InvalidSessionException {
        return this.getSession(sessionKey).getAttributeKeys();
    }

    @Override
    public Object getAttribute(SessionKey sessionKey, Object attributeKeys) throws InvalidSessionException {
        return this.getSession(sessionKey).getAttribute(attributeKeys);
    }

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKeys, Object value) throws InvalidSessionException {
        if (null == value) {
            this.removeAttribute(sessionKey, attributeKeys);
        } else {
            Session session = this.getSession(sessionKey);
            session.setAttribute(attributeKeys, value);
            this.onSessionChange(session);
        }
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKeys) throws InvalidSessionException {
        Session session = this.getSession(sessionKey);
        Object removed = session.getAttribute(attributeKeys);
        if (null != removed) {
            session.removeAttribute(attributeKeys);
        }
        return removed;
    }

    /**
     * session发生变化
     *
     * @param session
     */
    protected void onSessionChange(Session session) {
        this.updateSessionFormCacheDao(session);
    }

    protected void onExpiredSession(Session session, ExpiredSessionException ese, SessionKey sessionKey) {
        try {
            session.expire();
            this.onSessionExpire(session);
        } finally {
            this.afterSessionExpire(session);
        }
    }

    protected void onInvalidationSession(Session session, InvalidSessionException ise, SessionKey sessionKey) {
        if (ise instanceof ExpiredSessionException) {
            this.onExpiredSession(session, (ExpiredSessionException) ise, sessionKey);
            return;
        }
        log.trace("Session with id [{}] is invalid.", session.getId());
        try {
            this.onSessionStop(session);
        } finally {
            this.afterSessionStop(session);
        }
    }

    /**
     * 当session停止时调用
     *
     * @param session
     */
    protected void onSessionStop(Session session) {
        this.onSessionChange(session);
    }

    /**
     * 当session停止时调用
     *
     * @param session
     */
    protected void afterSessionStop(Session session) {
        if (this.isDeleteInvalidSessions()) {
            this.deleteSessionFormCacheDao(session);
        }
    }

    /**
     * 当session过期是调用
     *
     * @param session
     */
    protected void onSessionExpire(Session session) {
        this.onSessionChange(session);
    }

    /**
     * 当session停止时调用
     *
     * @param session
     */
    protected void afterSessionExpire(Session session) {
    }

    /**
     * 获取session
     *
     * @param sessionKey
     * @return
     */
    private Session doGetSession(SessionKey sessionKey) {
        return this.getSessionFormCacheDao(sessionKey);
    }

    private Session getSessionFormCacheDao(SessionKey sessionKey) {
        return this.cacheDao.readSession(sessionKey);
    }

    private void deleteSessionFormCacheDao(Session session) {
        this.cacheDao.deleteSession(session);
    }

    private void updateSessionFormCacheDao(Session session) {
        this.cacheDao.updateSession(session);
    }

    protected Collection<Session> getActiveSessions() {
        Collection<Session> activeSessions = this.cacheDao.getAllSessions();
        return null == activeSessions ? Collections.emptySet() : activeSessions;
    }

    /**
     * 启动session校验定时器
     */
    private void enabledSessionValidationScheduled() {
        if (null == this.scheduledExecutorService || !this.sessionValidateScheduledIsRun) {
            this.initSessionScheduled();
        }
    }

    /**
     * 禁用session校验定时器
     */
    private void disabledSessionValidationScheduled() {
        this.beforeSessionValidatorScheduledDisabled();
        if (null != this.scheduledExecutorService) {
            try {
                this.scheduledExecutorService.shutdownNow();
                log.info("disabled validateSession validation scheduled.");
            } catch (Exception e) {
                log.error("Unable to disabled SessionValidateScheduler. Ignoring (shutting down)...");
            }
        }
        this.sessionValidateScheduledIsRun = false;
    }

    private synchronized void initSessionScheduled() {
        if (null != this.scheduledExecutorService && this.sessionValidateScheduledIsRun) {
            return;
        }
        log.info("enabling validateSession validation scheduler...");
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor((run) -> {
            Thread thread = new Thread(run);
            thread.setDaemon(true);
            thread.setName("SessionValidationScheduleThread");
            return thread;
        });
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("clean invalid sessions...");
            Collection<Session> collection = getActiveSessions();
            if (CollectionUtils.isEmpty(collection)) {
                return;
            }
            collection.parallelStream().filter(Objects::nonNull).forEach(session -> {
               try{
                   doValidateSession(session, new DefaultSessionKey(session.getId()));
               } catch (Exception e){}
            });
        }, 1, 12, TimeUnit.HOURS);
        this.sessionValidateScheduledIsRun = true;
        this.afterSessionValidatorScheduledEnabled();
    }

    public void beforeSessionValidatorScheduledDisabled() {

    }

    public void afterSessionValidatorScheduledEnabled() {

    }

    public void beforeClearInvalidSessionWithScheduled(Session session) {

    }
}
