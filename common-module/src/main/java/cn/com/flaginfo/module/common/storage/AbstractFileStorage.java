package cn.com.flaginfo.module.common.storage;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Meng.Liu
 * @date: 2018/12/4 下午4:59
 */
@Slf4j
public abstract class AbstractFileStorage implements FileStorage {

    public static final String DEFAULT_IMG_FORMATTER = ".png";

    public static final String DEFAULT_AVATAR_FORMATTER = ".jpg";

    public static final String separator = "/";
    public static final String point = ".";

    /**
     * oss文件存储的基本路径
     */
    protected String basePath;
    /**
     * oss文件存储的域名
     */
    protected  String baseHost;
    /**
     * 别名域名
     */
    protected  String aliasBaseHost;
    /**
     * 别名
     */
    protected  String aliasBasePath;

    /**
     * 是否将路径转换成别名路径
     */
    protected volatile boolean transformAliasPath = false;

    /**
     * 返回目录文件夹
     * @return
     */
    public abstract String getFolder();

    /**
     * 获取访问路径
     * 多级路径按顺序拼接
     *
     * @param name
     * @return
     */
    public String getUrl(String... name) {
        return getUrl(false, name);
    }

    /**
     * 获取nginx访问路径
     * 多级路径按顺序拼接
     *
     * @param name
     * @return
     */
    public String getAliasUrl(String... name) {
        return getUrl(true, name);
    }

    public String getUrl(boolean alias, String... names) {
        StringBuilder builder = new StringBuilder(alias ? aliasBasePath : basePath);
        String url = joinPath(builder, names);
        if (log.isDebugEnabled()) {
            log.debug("storage url is : [{}], path : {}", url, names);
        }
        return url;
    }

    public String joinPath(String... names) {
        StringBuilder builder = new StringBuilder();
        return joinPath(builder, names);
    }

    public String joinPath(StringBuilder builder, String... names) {
        if (null == names || names.length == 0) {
            return builder.toString();
        }
        builder.append(getFolder());
        for (String p : names) {
            builder.append(p);
            if (!p.endsWith(separator)) {
                builder.append(separator);
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
