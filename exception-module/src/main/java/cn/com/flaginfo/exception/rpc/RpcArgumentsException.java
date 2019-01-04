package cn.com.flaginfo.exception.rpc;

/**
 * 参数异常
 * @author: Meng.Liu
 * @date: 2018/11/9 下午3:25
 */
public class RpcArgumentsException extends RpcException {

    private static final long serialVersionUID = 894798122053539232L;

    private static final long DEFAULT_RPC_ARGUMENTS_EXCEPTION_CODE = 900001;

    public RpcArgumentsException(String msg) {
        super(DEFAULT_RPC_ARGUMENTS_EXCEPTION_CODE, msg);
    }

    public RpcArgumentsException(String msg, Throwable throwable) {
        super(DEFAULT_RPC_ARGUMENTS_EXCEPTION_CODE, msg, throwable);
    }

    public RpcArgumentsException(Throwable throwable) {
        super(DEFAULT_RPC_ARGUMENTS_EXCEPTION_CODE, throwable);
    }
}
