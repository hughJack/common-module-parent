package cn.com.flaginfo.module.common.configuration;

import cn.com.flaginfo.module.common.utils.http.HttpConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

@Getter
@Setter
@ToString
public class DomainConfiguration implements InitializingBean {
    /**
     * 服务域名协议
     */
    private String scheme = HttpConstants.PROTOCOL_HTTPS;
    /**
     * 服务地址
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * url
     */
    private String url;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(url)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(scheme).append("://").append(host);
        if (StringUtils.isNotBlank(port)) {
            builder.append(":").append(port);
        }
        this.url = builder.toString();
    }
}