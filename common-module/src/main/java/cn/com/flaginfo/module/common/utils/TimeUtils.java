package cn.com.flaginfo.module.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/19 17:52
 */
public class TimeUtils {

    private final static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private static final List<String> formarts = new ArrayList<>(4);
    static{
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime timeFormatter(String time){
        String normalTime = timeFormatter2Normal(time);
       return timeFormatter(normalTime, getTimeFormatterWithTimeStr(normalTime));
    }

    public static LocalDateTime timeFormatter(String time, String pattern){
        return LocalDateTime.parse(timeFormatter2Normal(time), DateTimeFormatter.ofPattern(pattern));
    }

    public static long toEpochMilli(String time){
        return timeFormatter(time).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long toEpochMilli(String time, String pattern){
        return timeFormatter(time, pattern).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static String currentTimeStr(){
        return LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String currentTimeStr(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String time2Str(LocalDateTime time, String pattern){
        if( null == time ){
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String time2Str(LocalDateTime time){
        if( null == time ){
            return "";
        }
        return time.format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime mills2LocalDateTime(long mills){
        Instant instant = Instant.ofEpochMilli(mills);
        return instant2LocalDateTime(instant);
    }


    public static LocalDateTime second2LocalDateTime(long second){
        Instant instant = Instant.ofEpochSecond(second);
        return instant2LocalDateTime(instant);
    }

    public static LocalDateTime instant2LocalDateTime(Instant instant){
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取当前时间到24点的毫秒数
     * @return
     */
    public static long getCurrentTimeToNextDayMills(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    public static String timeFormatter2Normal(String time){
        if(StringUtils.isBlank(time)){
            return time;
        }
        return time.trim().replaceAll("年", "-")
                .replaceAll("月", "-")
                .replaceAll("日", "")
                .replaceAll("点", ":")
                .replaceAll("时", ":")
                .replaceAll("分", ":")
                .replaceAll("秒", "")
                .replaceAll("/", "-");
    }

    /**
     * 获取时间格式
     * @param time
     * @return
     */
    public static String getTimeFormatterWithTimeStr(String time){
        if( StringUtils.isBlank(time) ){
            throw new IllegalArgumentException("time string is blank.");
        }
        if(time.matches("^\\d{4}-\\d{1,2}$")){
            return formarts.get(0);
        }else if(time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return formarts.get(1);
        }else if(time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
            return formarts.get(2);
        }else if(time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return formarts.get(3);
        }else {
            throw new IllegalArgumentException("Invalid boolean value '" + time + "'");
        }
    }
}
