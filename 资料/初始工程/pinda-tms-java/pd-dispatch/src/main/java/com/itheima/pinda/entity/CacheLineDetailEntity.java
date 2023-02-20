package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓冲线路子表
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_cache_line_detail")
public class CacheLineDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheLineDetailEntity) {
            CacheLineDetailEntity cacheLineDetailEntity = (CacheLineDetailEntity) obj;
            return (id.equals(cacheLineDetailEntity.id));
        }
        return super.equals(obj);
    }

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
     * 起始机构
     */
    private String startAgencyId;

    /**
     * 终点机构
     */
    private String endAgencyId;

    /**
     * 从1开始递增
     */
    private int sort;

    /**
     * 业务表线路id
     */
    private String transportLineId;

    /**
     * 创建时间
     */
    private Date createDate;

}
