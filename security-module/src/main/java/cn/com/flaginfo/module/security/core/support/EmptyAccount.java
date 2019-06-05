package cn.com.flaginfo.module.security.core.support;

import java.util.Collections;
import java.util.Set;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 14:19
 */
public class EmptyAccount extends DefaultAccount {

    public EmptyAccount(){
        Set empty = Collections.emptySet();
        this.setPermissionInfos(empty);
        this.setPermissions(empty);
        this.setRoles(empty);
    }
}
