package cn.com.flaginfo.module.security.core;

import java.io.Serializable;

/**
 * 生成器
 * @author: Meng.Liu
 * @date: 2019-05-09 15:41
 */
public interface SessionIdGenerator {

    /**
     * sessionId 生成器
     * @return
     */
    Serializable generator();

}
