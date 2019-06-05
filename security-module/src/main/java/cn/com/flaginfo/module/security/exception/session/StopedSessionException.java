package cn.com.flaginfo.module.security.exception.session;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 14:30
 */
public class StopedSessionException extends InvalidSessionException {

    public StopedSessionException() {
        super();
    }

    public StopedSessionException(String message) {
        super(message);
    }

    public StopedSessionException(Throwable cause) {
        super(cause);
    }

    public StopedSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
