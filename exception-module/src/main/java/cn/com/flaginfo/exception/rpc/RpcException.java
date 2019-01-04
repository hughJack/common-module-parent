package cn.com.flaginfo.exception.rpc;

/**
 * RPC通用异常类
 * @author: Meng.Liu
 * @date: 2018/11/9 下午3:08
 */
public class RpcException extends Exception {

    private static final long serialVersionUID = 894798122053539231L;
    
    private static final long DEFAULT_EXCEPTION_CODE = 900000;
    
    /**
     * 错误码
     */
    private long code;

    public RpcException(String msg){
        this(DEFAULT_EXCEPTION_CODE, msg);
    }

    public RpcException(long code, String msg){
        super(msg);
        this.code = code;
    }

    public RpcException(String msg, Throwable throwable){
        this(DEFAULT_EXCEPTION_CODE, msg, throwable);
    }

    public RpcException(long code, String msg, Throwable throwable){
        super(msg, throwable);
        this.code = code;
    }

    public RpcException(Throwable throwable){
        this(DEFAULT_EXCEPTION_CODE, throwable);
    }

    public RpcException(long code, Throwable throwable){
        super(throwable);
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName() + " : [" + code + "], " + getMessage();
    }

}
