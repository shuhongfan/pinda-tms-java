package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车次车辆司机订单分类关联表
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_order_classify_attach")
public class OrderClassifyAttachEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String tripsId;

    private String truckId;

    private String driverId;

    private String orderClassifyId;

    /**
     * 创建时间
     */
    private Date createDate;

}
