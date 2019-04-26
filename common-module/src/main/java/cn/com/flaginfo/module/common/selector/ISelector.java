package cn.com.flaginfo.module.common.selector;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/16 15:17
 */
public interface ISelector<T> {

    /**
     * 获取选择的值
     * @return
     */
    T selected();

    /**
     * 获取选择值并删除选择值
     * @return
     */
    T getAndClearSelect();

    /**
     * 清空选择
     */
    void clearSelected();

    /**
     * 选择
     * @param value
     */
    void select(T value);
}
