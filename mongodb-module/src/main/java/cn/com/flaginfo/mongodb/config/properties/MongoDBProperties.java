package cn.com.flaginfo.mongodb.config.properties;

import cn.com.flaginfo.module.common.BaseProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/11 17:48
 */
@Getter
@Setter
public class MongoDBProperties extends BaseProperties {

    /**
     * 数据源Id
     */
    private String id;
    /**
     * 服务器地址，ip:port,ip:port
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private char[] password;
    /**
     * 数据库
     */
    private String database;
}
