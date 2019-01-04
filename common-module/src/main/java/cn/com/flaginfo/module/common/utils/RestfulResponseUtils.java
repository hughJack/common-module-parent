package cn.com.flaginfo.module.common.utils;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.module.common.domain.restful.HttpResponseVO;
import cn.com.flaginfo.module.common.domain.restful.PageResponseVO;
import cn.com.flaginfo.module.common.domain.restful.RestfulResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:49
 */
@Slf4j
public class RestfulResponseUtils {

    private static final String SUCCESS_MSG = "请求成功";

    /**
     * 成功，返回空数据
     *
     * @return
     */
    public static RestfulResponse success() {
        return success(HttpResponseVO.emptyHttpResponseVO());
    }

    /**
     * 成功返回Object
     *
     * @param data
     * @return
     */
    public static RestfulResponse success(Object data) {
        return success(data, SUCCESS_MSG);
    }

    /**
     * 成功返回Object数据
     *
     * @param data
     * @param message
     * @return
     */
    public static RestfulResponse success(Object data, String message) {
        RestfulResponse.RestfulResponseBuilder responseBuilder = RestfulResponse.successBuilder();
        return responseBuilder.setData(data)
                .setMessage(message).build();
    }

    /**
     * 成功返回列表
     *
     * @param data
     * @param dataCount
     * @return
     */
    public static RestfulResponse success(List<Object> data, int dataCount) {
        return success(data, dataCount, SUCCESS_MSG);
    }

    /**
     * 成功，返回查询列表
     *
     * @param data
     * @param dataCount
     * @param message
     * @return
     */
    public static RestfulResponse success(List<Object> data, int dataCount, String message) {
        PageResponseVO<List<Object>> responseVO = new PageResponseVO<>();
        responseVO.setData(data);
        responseVO.setDataCount(dataCount);
        RestfulResponse.RestfulResponseBuilder responseBuilder = RestfulResponse.successBuilder();
        return responseBuilder.setData(responseVO)
                .setMessage(message).build();
    }

    /**
     * 失败
     *
     * @param restfulCode
     * @return
     */
    public static RestfulResponse error(ErrorCode restfulCode) {
        return error(restfulCode.code(), restfulCode.message());
    }

    /**
     * 失败
     *
     * @param message
     * @return
     */
    public static RestfulResponse error(String message) {
        return error(ErrorCode.SYS_BUSY.code(), message);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static RestfulResponse error(long code, String message) {
        return RestfulResponse.builder()
                .setCode(code)
                .setMessage(message)
                .build();
    }

    /**
     * 判断是否成功
     *
     * @param restfulResponse
     * @return
     */
    public static boolean isSuccess(RestfulResponse restfulResponse) {
        if (null == restfulResponse) {
            return false;
        }
        return ErrorCode.SUCCESS.getCode().equals(restfulResponse.getCode());
    }
}
