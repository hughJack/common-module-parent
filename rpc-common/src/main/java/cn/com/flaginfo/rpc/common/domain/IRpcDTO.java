package cn.com.flaginfo.rpc.common.domain;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * RPC调用的传输对象
 * @author: Meng.Liu
 * @date: 2018/11/9 下午3:04
 */
public interface IRpcDTO extends Serializable {

    IRpcDTO EMPTY_DTO = new EmptyRpcBaseDTO();

    /**
     * 获取空数据对象
     * @return
     */
    static IRpcDTO emptyDTO(){
        return EMPTY_DTO;
    }

    /**
     * 对象转成格式化json字符串
     * @param object
     * @return
     */
    static String toJSON(Object object){
        return JSONObject.toJSONString(object, true);
    }

    /**
     * 空数据对象
     * @author: Meng.Liu
     * @date: 2018/11/9 下午3:04
     */
    class EmptyRpcBaseDTO implements IRpcDTO {

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
