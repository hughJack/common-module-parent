package cn.com.flaginfo.module.security.exception.session;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 13:57
 */
public class ExpiredSessionException extends InvalidSessionException {

    public ExpiredSessionException() {
        super();
    }

    public ExpiredSessionException(String message) {
        super(message);
    }

    public ExpiredSessionException(Throwable cause) {
        super(cause);
    }

    public ExpiredSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
