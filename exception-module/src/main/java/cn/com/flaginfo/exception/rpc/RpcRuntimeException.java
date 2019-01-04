package cn.com.flaginfo.exception.rpc;

/**
 * @author: Meng.Liu
 * @date: 2018/12/12 上午9:37
 */
public class RpcRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 894798122053539231L;

    /**
     * 错误码
     */
    private long code;

    public RpcRuntimeException(long code, String msg){
        super(msg);
        this.code = code;
    }

    public RpcRuntimeException(long code, String msg, Throwable throwable){
        super(msg, throwable);
        this.code = code;
    }

    public RpcRuntimeException(long code, Throwable throwable){
        super(throwable);
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
