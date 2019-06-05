package cn.com.flaginfo.module.security.exception.session;

import cn.com.flaginfo.module.security.exception.SecurityException;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 13:57
 */
public class SessionException extends SecurityException {

    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
