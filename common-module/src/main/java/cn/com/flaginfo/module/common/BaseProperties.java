package cn.com.flaginfo.module.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/15 18:07
 */
@Getter
@Setter
public class BaseProperties {

    private String id;
    private boolean isDefault = false;

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
