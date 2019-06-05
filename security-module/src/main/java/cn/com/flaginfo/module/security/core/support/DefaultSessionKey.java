package cn.com.flaginfo.module.security.core.support;

import cn.com.flaginfo.module.security.core.SessionKey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2019-05-10 11:27
 */
@Getter
@Setter
@ToString
public class DefaultSessionKey implements SessionKey {
    private Serializable sessionId;

    public DefaultSessionKey(){
    }

    public DefaultSessionKey(Serializable sessionId){
        this.sessionId = sessionId;
    }
}
