package cn.com.flaginfo.rpc.common.domain;

/**
 * RPC基类
 * @author: Meng.Liu
 * @date: 2019/1/9 上午10:38
 */
public class RpcBaseDTO implements IRpcDTO {

    @Override
    public String toString() {
        return IRpcDTO.toJSON(this);
    }
}
