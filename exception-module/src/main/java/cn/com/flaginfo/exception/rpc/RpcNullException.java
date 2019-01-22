package cn.com.flaginfo.exception.rpc;

/**
 * @author: Meng.Liu
 * @date: 2018/11/9 下午5:55
 */
public class RpcNullException extends RpcException {

    private static final long serialVersionUID = 894798122053539233L;

    public static final long DEFAULT_EXCEPTION_CODE = 900003;

    public RpcNullException(String msg) {
        super(DEFAULT_EXCEPTION_CODE, msg);
    }

    public RpcNullException(String msg, Throwable throwable) {
        super(DEFAULT_EXCEPTION_CODE, msg, throwable);
    }

    public RpcNullException(Throwable throwable) {
        super(DEFAULT_EXCEPTION_CODE, throwable);
    }

}
