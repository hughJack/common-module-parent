package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: Meng.Liu
 * @date: 2019-05-09 15:42
 */
public class UuidSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generator() {
        return UUID.randomUUID().toString();
    }
}
