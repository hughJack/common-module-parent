package cn.com.flaginfo.module.security.core;

import java.io.Serializable;

/**
 * 鉴权信息
 * 账号鉴权信息
 * @author: Meng.Liu
 * @date: 2019-05-09 14:00
 */
public interface AuthenticationInfo extends Serializable {

    /**
     * 账号主要信息
     * @return
     */
    Object getPrincipals();
}
