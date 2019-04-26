package cn.com.flaginfo.rpc.common.utils;

import cn.com.flaginfo.platform.api.common.base.PageInfo;

/**
 * @author: Meng.Liu
 * @date: 2019/3/26 下午3:37
 */
public class PagableUtils {

    /**
     * 获取skip
     * @param pageInfo
     * @return
     */
    public static Integer getSkip(PageInfo pageInfo){
        Integer curPage = pageInfo.getCurPage();
        if( null == curPage || curPage < 1){
            curPage = 1;
        }
        return ( curPage - 1 ) * getLimit(pageInfo);
    }

    /**
     * 获取limit
     * @param pageInfo
     * @return
     */
    public static Integer getLimit(PageInfo pageInfo){
        Integer limit = pageInfo.getPageLimit();
        return null == limit ? 10 : limit;
    }
}
