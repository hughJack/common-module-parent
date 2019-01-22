package cn.com.flaginfo.module.common.reflect;

/**
 * @author: Meng.Liu
 * @date: 2019/1/21 下午4:06
 */
public class DescriptionException extends RuntimeException {

    public DescriptionException() {
        super();
    }

    public DescriptionException(String message) {
        super(message);
    }

    public DescriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DescriptionException(Throwable cause) {
        super(cause);
    }

    protected DescriptionException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
