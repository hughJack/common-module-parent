package cn.com.flaginfo.module.common.domain.restful;

import lombok.Data;

/**
 * @author: Meng.Liu
 * @date: 2018/11/12 上午9:52
 */
@Data
public class PageRequestVO<S> extends HttpRequestVO{

    private static final long serialVersionUID = 1L;

    private Integer skip;

    private Integer limit;

    private S search;

}
