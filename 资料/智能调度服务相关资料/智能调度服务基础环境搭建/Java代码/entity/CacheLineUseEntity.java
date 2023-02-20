package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓冲线路使用表
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_cache_line_use")
public class CacheLineUseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 缓冲线路表id
     */
    private String cacheLineId;

    /**
     * 订单分类表id
     */
    private String orderClassifyId;

    /**
     * 创建时间
     */
    private Date createDate;

}
