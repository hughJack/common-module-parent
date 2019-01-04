package cn.com.flaginfo.mongodb.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/11 10:17
 */
@Setter
@Getter
public class BaseMongoPO implements IBaseMongoPO {

    @Id
    protected String id;
    @CreatedDate
    protected Date create_time;
    @LastModifiedDate
    protected Date update_time;
}
