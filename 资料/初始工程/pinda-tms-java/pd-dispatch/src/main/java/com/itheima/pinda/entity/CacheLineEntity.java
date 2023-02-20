package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 缓冲线路表
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_cache_line")
public class CacheLineEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        return verifyKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheLineEntity) {
            CacheLineEntity cacheLineEntity = (CacheLineEntity) obj;
            return (verifyKey.equals(cacheLineEntity.verifyKey));
        }
        return super.equals(obj);
    }

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 起始机构
     */
    private String startAgencyId;

    /**
     * 终点机构
     */
    private String endAgencyId;

    /**
     * 验证是否可用key，md5(子表的起始相加)
     */
    private String verifyKey;

    /**
     * 距离
     */
    private BigDecimal distance;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 预计时间
     */
    private BigDecimal estimatedTime;

    /**
     * 中转次数
     */
    private Integer transferCount;

    /**
     * 从1开始递增
     */
    private Integer version;

    /**
     * 是否是当前最新版本 0 否 1 是
     */
    private int isCurrent;

    /**
     * 创建时间
     */
    private Date createDate;

}
