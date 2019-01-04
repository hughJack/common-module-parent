package cn.com.flaginfo.exception.restful;

/**
 * @author: Meng.Liu
 * @date: 2018/11/9 下午5:55
 */
public class RestfulNullException extends RestfulException {

    private static final long serialVersionUID = 894798122053539233L;

    private static final long DEFAULT_REST_NULL_EXCEPTION_CODE = 800001;

    public RestfulNullException(String msg) {
        super(DEFAULT_REST_NULL_EXCEPTION_CODE, msg);
    }

    public RestfulNullException(String msg, Throwable throwable) {
        super(DEFAULT_REST_NULL_EXCEPTION_CODE, msg, throwable);
    }

    public RestfulNullException(Throwable throwable) {
        super(DEFAULT_REST_NULL_EXCEPTION_CODE, throwable);
    }

}
