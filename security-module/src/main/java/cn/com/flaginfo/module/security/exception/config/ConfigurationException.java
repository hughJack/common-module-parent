package cn.com.flaginfo.module.security.exception.config;

import cn.com.flaginfo.module.security.exception.SecurityException;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 14:57
 */
public class ConfigurationException extends SecurityException {

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
