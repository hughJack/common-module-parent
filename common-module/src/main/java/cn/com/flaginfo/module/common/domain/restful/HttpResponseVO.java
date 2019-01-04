package cn.com.flaginfo.module.common.domain.restful;

import lombok.Data;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:41
 */
@Data
public class HttpResponseVO extends HttpBaseVO{

    private static final long serialVersionUID = 1L;

    private static final HttpResponseVO EMPTY_HTTP_RESPONSE_VO = new EmptyHttpResponseVO();

    /**
     * 获取空数据对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public final static HttpResponseVO emptyHttpResponseVO(){
        return EMPTY_HTTP_RESPONSE_VO;
    }

    /**
     * 空数据对象
     * @author: Meng.Liu
     * @date: 2018/11/9 下午3:04
     */
    private static class EmptyHttpResponseVO extends HttpResponseVO {

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
            return obj instanceof HttpResponseVO.EmptyHttpResponseVO;
        }
    }
}
