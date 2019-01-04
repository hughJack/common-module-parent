package cn.com.flaginfo.module.common.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * @author meng.liu
 */
public class LogUtils {


    private static LoggerContext getLoggerContext(){
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public static String getLogLevel(){
        return getLoggerContext().getLogger("root").getLevel().toString();
    }

    public static void setLoggerDebug(){
        setLogger("DEBUG");
    }

    public static void setLoggerInfo(){
        setLogger("INFO");
    }

    public static void setLoggerWarn(){
        setLogger("WARN");
    }

    public static void setLoggerError(){
        setLogger("ERROR");
    }

    public static void setLogger(String level){
        setLogger("root", level);
    }

    public static void setLogger(String packageName, String level){
        setLogger(packageName, Level.toLevel(level));
    }

    public static void setLogger(String packageName, Level level){
        getLoggerContext().getLogger(packageName).setLevel(level);
    }
}
