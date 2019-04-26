package cn.com.flaginfo.module.common.selector;

import cn.com.flaginfo.module.common.singleton.AbstractSingleton;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/16 15:21
 */
public abstract class AbstractSelector<T> extends AbstractSingleton implements ISelector<T> {

    private final Object lock = new Object();

    public AbstractSelector() throws SingletonException {
        super();
    }

    @Override
    public T getAndClearSelect() {
        synchronized (lock) {
            T t = this.selected();
            this.clearSelected();
            return t;
        }
    }

}
