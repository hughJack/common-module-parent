package cn.com.flaginfo.module.security.exception.session;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 13:57
 */
public class InvalidSessionException extends SessionException {

    public InvalidSessionException() {
        super();
    }

    public InvalidSessionException(String message) {
        super(message);
    }

    public InvalidSessionException(Throwable cause) {
        super(cause);
    }

    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
