package cn.com.flaginfo.rpc.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Meng.Liu
 * @date: 2018/12/11 下午1:37
 */
@Configuration
@ConfigurationProperties(prefix = "spring.app.rpc")
@Getter
@Setter
public class RpcConfiguration {

    public static final String SLOW_THRESHOLD_CONF = "spring.app.rpc.slow.threshold";

    public static final String LOG_LEVEL_CONF = "spring.app.rpc.log.level";

    /**
     * rpc慢查询阈值，单位ms
     */
    private long slowThreshold = 500;

    /**
     * 日志级别
     */
    private RpcLogLevel logLevel = RpcLogLevel.Succinct;

    public enum RpcLogLevel{
        /**
         * 关闭
         */
        Off,
        /**
         * 详细的
         */
        Detailed,
        /**
         * 简洁的
         */
        Succinct,
        /**
         * 当查询为慢查询时才打印详细
         */
        Detailed_When_Slow,
        /**
         * 当查询为慢查询时才打印简洁
         */
        Succinct_When_Slow

    }

    /**
     * 获取日志级别
     * @param name
     * @return
     */
    public static RpcLogLevel getLogLevelWithName(String name){
        if(StringUtils.isBlank(name)){
            return RpcLogLevel.Succinct;
        }
        name = name.trim();
        for( RpcLogLevel level : RpcLogLevel.values() ){
            if( StringUtils.equalsIgnoreCase(level.name(), name) ){
                return level;
            }
        }
        return RpcLogLevel.Succinct;
    }
}
