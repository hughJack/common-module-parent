package cn.com.flaginfo.module.security.exception.session;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 13:57
 */
public class UnknownSessionException extends InvalidSessionException {

    public UnknownSessionException() {
        super();
    }

    public UnknownSessionException(String message) {
        super(message);
    }

    public UnknownSessionException(Throwable cause) {
        super(cause);
    }

    public UnknownSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
