package cn.com.flaginfo.module.security.core;

import java.util.Collection;

/**
 * 权限信息
 * @author: Meng.Liu
 * @date: 2019-05-09 13:51
 */
public interface PermissionInfo {

    /**
     * 获取权限列表
     * @return
     */
    Collection<String> getPermissions();

}
