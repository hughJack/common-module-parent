package cn.com.flaginfo.mongodb.vo;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/11 14:33
 */
@Data
public class Page<D> implements Serializable {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private Integer pageSize;

    private Integer pageNumber;

    private Long total;

    private List<D> data;

    private D filter;

    private Sort sort;

    public int getPageSize() {
        return null == pageSize ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public int getPageNumber() {
        return null == pageNumber ? DEFAULT_PAGE_NUMBER : pageNumber;
    }

    public Integer offset(){
        return (this.getPageNumber() - 1) * getPageSize();
    }
}
