package cn.com.flaginfo.exception.common;

/**
 * @author: Meng.Liu
 * @date: 2019/1/9 下午1:35
 */
public class ArgumentException extends Exception {

    private static final long serialVersionUID = 894798122053539231L;

    public static final long DEFAULT_EXCEPTION_CODE = 700001;

    /**
     * 错误码
     */
    private long code;

    public ArgumentException(String msg){
        this(DEFAULT_EXCEPTION_CODE, msg);
    }

    public ArgumentException(long code, String msg) {
        super(msg);
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
