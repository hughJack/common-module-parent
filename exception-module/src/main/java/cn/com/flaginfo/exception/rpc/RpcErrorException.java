package cn.com.flaginfo.exception.rpc;

/**
 * RPC响应失败
 * @author: Meng.Liu
 * @date: 2018/12/12 上午11:43
 */
public class RpcErrorException extends RpcException {

    public RpcErrorException(long code, String msg) {
        super(code, msg);
    }

}
