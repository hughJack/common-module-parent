package cn.com.flaginfo.rpc.common.domain;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2018/11/9 下午3:04
 */
public class RpcBaseDTO implements Serializable {

    public static final RpcBaseDTO EMPTY_DTO = new EmptyRpcBaseDTO();

    /**
     * 子类必须实现toString方法
     * @params：
     * @return：
     * @author：Meng.Liu
     * @date：2018/11/9 下午3:07
     * @modify and other information：
     */
    @Override
    public String toString(){
        return JSONObject.toJSONString(this, true);
    }

    /**
     * 获取空数据对象
     * @return
     */
    public final static RpcBaseDTO emptyDTO(){
        return EMPTY_DTO;
    }

    /**
     * 空数据对象
     * @author: Meng.Liu
     * @date: 2018/11/9 下午3:04
     */
    private static class EmptyRpcBaseDTO extends RpcBaseDTO {

        @Override
        public String toString() {
            return this.getClass().getName() + ":[Empty Data]";
        }

        /**
         * 是否为空
         * @return
         */
        public boolean isEmpty(){
            return true;
        }

        /**
         * 是否为空
         * @return
         */
        public boolean isNull(){
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if( null == obj ){
                return true;
            }
            return obj instanceof EmptyRpcBaseDTO;
        }
    }
}
