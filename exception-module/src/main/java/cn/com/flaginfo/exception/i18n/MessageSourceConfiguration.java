package cn.com.flaginfo.exception.i18n;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author: Meng.Liu
 * @date: 2018/12/17 下午3:04
 */
@Configuration
@ConfigurationProperties(prefix = "spring.message")
@Getter
@Setter
@ToString
public class MessageSourceConfiguration {

    /**
     * 资源路径
     */
    private String[] baseNames = new String[]{};
    /**
     * 缓存时间
     */
    private long cacheMills = -1;
    /**
     * 默认编码格式
     */
    private String defaultEncoding = "UTF-8";

    /**
     * 默认语言环境
     */
    private Locale defaultLocale = Locale.CHINA;

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        List<String> names = new ArrayList<>(baseNames.length + 1);
        Collections.addAll(names, baseNames);
        names.add("classpath:/default-i18n/message");
        String[] newNames = new String[names.size()];
        messageSource.setBasenames(names.toArray(newNames));
        messageSource.setCacheMillis(cacheMills);
        messageSource.setDefaultEncoding(defaultEncoding);
        return messageSource;
    }

}
