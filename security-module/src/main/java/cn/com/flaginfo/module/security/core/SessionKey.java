package cn.com.flaginfo.module.security.core;

import java.io.Serializable;

/**
 * 回话唯一标示
 * @author: Meng.Liu
 * @date: 2019-05-09 14:04
 */
public interface SessionKey extends Serializable {

    /**
     * 获取回话Id
     * @return
     */
    Serializable getSessionId();
}
