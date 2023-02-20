package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单分类订单关联表
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_order_classify_order")
public class OrderClassifyOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 订单分类id
     */
    private String orderClassifyId;

    /**
     * 订单id
     */
    private String orderId;
}
