package cn.com.flaginfo.module.common.domain.restful;

import cn.com.flaginfo.exception.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:41
 */
@Data
@Slf4j
public class RestfulResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long code;

    private String message;

    private Object data;

    private RestfulResponse() {
    }

    public static class RestfulResponseBuilder{

        private Long code;
        private String message;
        private Object data;

        private RestfulResponseBuilder(){ }

        public RestfulResponseBuilder setCode(long code){
            this.code = code;
            return this;
        }

        public RestfulResponseBuilder setMessage(String message){
            this.message = message;
            return this;
        }

        public RestfulResponseBuilder setData(Object data){
            this.data = data;
            return this;
        }

        @SuppressWarnings("unchecked")
        private Object createEmptyData(){
            log.warn("response is success, but there is no explicit setting of data. this is not recommended.");
            return (Object) new HttpResponseVO();
        }

        public RestfulResponse build() throws NullPointerException{
            if( null == this.code ){
                throw new NullPointerException("response code cannot be null.");
            }
            if(ErrorCode.SUCCESS.code().equals(this.code) && null == this.data ){
                this.data = this.createEmptyData();
            }
            RestfulResponse restfulResponse = new RestfulResponse();
            restfulResponse.setCode(this.code);
            restfulResponse.setData(this.data);
            restfulResponse.setMessage(this.message);
            return restfulResponse;
        }

    }

    public static  RestfulResponseBuilder builder(){
        return new RestfulResponseBuilder();
    }

    public static  RestfulResponseBuilder successBuilder(){
        RestfulResponseBuilder responseBuilder = new RestfulResponseBuilder();
        return responseBuilder.setCode(ErrorCode.SUCCESS.code());
    }

    public static RestfulResponseBuilder errorBuilder(){
        RestfulResponseBuilder responseBuilder = new RestfulResponseBuilder();
        return responseBuilder.setCode(ErrorCode.SYS_BUSY.code());
    }
}