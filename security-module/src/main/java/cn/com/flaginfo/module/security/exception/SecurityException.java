package cn.com.flaginfo.module.security.exception;

/**
 * @author: Meng.Liu
 * @date: 2019-05-08 13:56
 */
public class SecurityException extends RuntimeException {

    public SecurityException(){

    }

    public SecurityException(String message){
        super(message);
    }

    public SecurityException(Throwable cause){
        super(cause);
    }

    public SecurityException(String message, Throwable cause){
        super(message, cause);
    }
}
