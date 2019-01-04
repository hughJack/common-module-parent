package cn.com.flaginfo.module.common.diamond;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 初始化配置
 * @author Meng.Liu
 * @create 2017-09-26 10:54
 **/
@Slf4j
public class DiamondPropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public DiamondPropertiesInitializer() {
        log.info("init diamond properties start...");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        DiamondProperties.initInstance(applicationContext.getEnvironment());
    }
}