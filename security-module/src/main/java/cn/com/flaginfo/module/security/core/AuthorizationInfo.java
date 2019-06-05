package cn.com.flaginfo.module.security.core;

import java.io.Serializable;
import java.util.Collection;

/**
 * 授权信息
 * 账号的权限信息
 * @author: Meng.Liu
 * @date: 2019-05-09 13:50
 */
public interface AuthorizationInfo extends Serializable {
    /**
     * 获取角色列表
     * @return
     */
    Collection<String> getRoles();

    /**
     * 获取权限项列表
     * @return
     */
    Collection<String> getPermissions();

    /**
     * 获取权限对象列表
     * @return
     */
    Collection<PermissionInfo> getPermissionInfos();
}
