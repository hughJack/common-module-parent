package cn.com.flaginfo.exception.restful;

import cn.com.flaginfo.exception.ErrorCode;

/**
 * @author: Meng.Liu
 * @date: 2018/12/6 下午2:20
 */
public class RestfulRuntimeException extends RuntimeException {

    /**
     * 错误码
     */
    private long code;

    public RestfulRuntimeException(ErrorCode restfulCode){
        super(restfulCode.message());
        this.code = restfulCode.code();
    }

    public RestfulRuntimeException(long code, String msg){
        super(msg);
        this.code = code;
    }

    public RestfulRuntimeException(long code, String msg, Throwable throwable){
        super(msg, throwable);
        this.code = code;
    }

    public RestfulRuntimeException(long code, Throwable throwable){
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
