package cn.com.flaginfo.module.common.domain.restful;

import lombok.Data;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:53
 */
@Data
public class PageResponseVO<D> extends HttpResponseVO {

    private static final long serialVersionUID = 1L;

    private Integer dataCount;

    private D data;

}
