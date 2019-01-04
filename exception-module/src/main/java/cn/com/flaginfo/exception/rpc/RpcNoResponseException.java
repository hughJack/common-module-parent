package cn.com.flaginfo.exception.rpc;

/**
 * RPC无响应
 * @author: Meng.Liu
 * @date: 2018/12/12 上午11:44
 */
public class RpcNoResponseException extends RpcException{

    private static final long DEFAULT_EXCEPTION_CODE = 900002;

    public RpcNoResponseException(String msg) {
        super(DEFAULT_EXCEPTION_CODE, msg);
    }
}
