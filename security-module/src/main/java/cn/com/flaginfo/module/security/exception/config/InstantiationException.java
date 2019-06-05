package cn.com.flaginfo.module.security.exception.config;

import cn.com.flaginfo.module.security.exception.SecurityException;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 15:46
 */
public class InstantiationException extends SecurityException {

    public InstantiationException() {
        super();
    }

    public InstantiationException(String message) {
        super(message);
    }

    public InstantiationException(Throwable cause) {
        super(cause);
    }

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

}
