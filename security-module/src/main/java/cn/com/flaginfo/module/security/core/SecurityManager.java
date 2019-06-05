package cn.com.flaginfo.module.security.core;

/**
 * 权限架构主入口
 * @author: Meng.Liu
 * @date: 2019-05-10 11:33
 */
public interface SecurityManager {

    /**
     * 获取session manager
     * @return
     */
    SessionManager getSessionManager();

    /**
     * 获取session key
     * @return
     */
    SessionKey getSessionKey();
}
