package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单分类记录
 *
 * @author diesel 2020-04-16
 */
@Data
@TableName("pd_order_classify")
public class OrderClassifyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 任务id
     */
    private String jobId;

    /**
     * 任务日志id
     */
    private String jobLogId;

    /**
     * 起始机构
     */
    private String startAgencyId;

    /**
     * 终点机构
     */
    private String endAgencyId;
    
    /**
     * 订单类别:xxx#xxx#xxx
     */
    private String classify;

    /**
     * 订单数量
     */
    private Integer total;

    /**
     * 创建时间
     */
    private Date createDate;

}
