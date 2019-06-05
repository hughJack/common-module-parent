package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.Account;
import cn.com.flaginfo.module.security.core.PermissionInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 14:15
 */
@Getter
@Setter
@ToString
public class DefaultAccount implements Account {
    /**
     * 主要用户信息
     */
    private Object principals;
    /**
     * 角色信息
     */
    private Collection<String> roles;
    /**
     * 权限信息
     */
    private Collection<String> permissions;
    /**
     * 权限对象信息
     * @return
     */
    private Collection<PermissionInfo> permissionInfos;
}
