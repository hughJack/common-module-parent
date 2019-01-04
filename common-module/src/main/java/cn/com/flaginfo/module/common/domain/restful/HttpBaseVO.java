package cn.com.flaginfo.module.common.domain.restful;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:39
 */
@Data
public class HttpBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString(){
        return JSONObject.toJSONString(this, true);
    }

}
