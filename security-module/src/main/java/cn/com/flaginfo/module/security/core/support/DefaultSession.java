package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.Session;
import cn.com.flaginfo.module.security.exception.session.ExpiredSessionException;
import cn.com.flaginfo.module.security.exception.session.InvalidSessionException;
import cn.com.flaginfo.module.security.exception.session.StopedSessionException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 10:53
 */
@Getter
@Setter
@ToString
@Slf4j
public class DefaultSession implements Session {

    private transient Serializable id;
    private transient Date startTimestamp;
    private transient Date stopTimestamp;
    private transient Date lastAccessTimestamp;
    private transient long timeout;
    private transient boolean expired;
    private transient String host;
    private transient Map<Object, Object> attributes;

    public DefaultSession() {
        this.timeout = DefaultSessionManager.DEFAULT_SESSION_TIMEOUT;
        this.startTimestamp = new Date();
        this.lastAccessTimestamp = this.startTimestamp;
    }

    public DefaultSession(String host) {
        this();
        this.host = host;
    }

    @Override
    public void stop() throws InvalidSessionException {
        if (null == this.stopTimestamp) {
            this.stopTimestamp = new Date();
        }
    }

    @Override
    public void touch() throws InvalidSessionException {
        this.lastAccessTimestamp = new Date();
    }

    /**
     * 是否已经停止
     *
     * @return
     */
    @Override
    public boolean isStopped() {
        return getStopTimestamp() != null;
    }

    /**
     * 是否超时
     *
     * @return
     */
    @Override
    public boolean isTimeout() {
        if (this.isExpired()) {
            return true;
        }
        long timeout = this.getTimeout();
        if (timeout >= 0L) {
            Date lastAccessTime = this.getLastAccessTimestamp();
            if (lastAccessTime == null) {
                throw new IllegalStateException("validateSession.lastAccessTime for validateSession with id [" + getId() + "] is null.  This value must be set at least once");
            }
            long expireTimeMillis = System.currentTimeMillis() - timeout;
            Date expireTime = new Date(expireTimeMillis);
            return lastAccessTime.before(expireTime);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("No timeout for validateSession with id [" + getId() + "].  Session is not considered expired.");
            }
        }
        return false;
    }

    /**
     * 设置为过期
     */
    @Override
    public void expire() {
        this.stop();
        this.expired = true;
    }

    @Override
    public boolean isValid() {
        return !this.isStopped() && !this.isExpired();
    }

    @Override
    public void validate() throws InvalidSessionException {
        if (this.isStopped()) {
            throw new StopedSessionException("Session with id [" + getId() + "] has been explicitly stopped. ");
        }
        if (this.isTimeout()) {
            this.expire();
            Date lastAccessTime = this.getLastAccessTimestamp();
            DateFormat df = DateFormat.getInstance();
            throw new ExpiredSessionException("Session with id [" + getId() + "] has been timeout. last access time: " + df.format(lastAccessTime) + ", current time: " + df.format(new Date()) + "], Session timeout is set to " + this.timeout + " mills");
        }
    }

    private Map<Object, Object> getOrCreateAttributes() {
        Map<Object, Object> attributes = this.getAttributes();
        if ( null == attributes) {
            attributes = new HashMap<>(16);
            this.setAttributes(attributes);
        }
        return attributes;
    }

    @Override
    public Collection<Object> getAttributeKeys() throws InvalidSessionException {
        Map<Object, Object> attributes = this.getAttributes();
        if (attributes == null) {
            return Collections.emptySet();
        }
        return attributes.keySet();
    }

    @Override
    public Object getAttribute(Object key) {
        Map<Object, Object> attributes = this.getAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.get(key);
    }

    @Override
    public void setAttribute(Object key, Object value) {
        if (value == null) {
            removeAttribute(key);
        } else {
            this.getOrCreateAttributes().put(key, value);
        }
    }

    @Override
    public Object removeAttribute(Object key) {
        Map<Object, Object> attributes = this.getAttributes();
        if (attributes == null) {
            return null;
        } else {
            return attributes.remove(key);
        }
    }
}
