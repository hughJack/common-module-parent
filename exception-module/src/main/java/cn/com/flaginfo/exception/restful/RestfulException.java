package cn.com.flaginfo.exception.restful;

import cn.com.flaginfo.exception.ErrorCode;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午10:33
 */
public class RestfulException extends Exception{

    private static final long serialVersionUID = 894798122053539231L;

    private static final long DEFAULT_EXCEPTION_CODE = 800000;

    /**
     * 错误码
     */
    private long code;

    public RestfulException(ErrorCode restfulCode){
        this(restfulCode.code(), restfulCode.message());
    }

    public RestfulException(String msg){
        this(DEFAULT_EXCEPTION_CODE, msg);
    }

    public RestfulException(long code, String msg){
        super(msg);
        this.code = code;
    }

    public RestfulException(String msg, Throwable throwable){
        this(DEFAULT_EXCEPTION_CODE, msg, throwable);
    }

    public RestfulException(long code, String msg, Throwable throwable){
        super(msg, throwable);
        this.code = code;
    }

    public RestfulException(Throwable throwable){
        this(DEFAULT_EXCEPTION_CODE, throwable);
    }

    public RestfulException(long code, Throwable throwable){
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
