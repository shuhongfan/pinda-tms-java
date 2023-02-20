package com.itheima.pinda.entity.user;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 司机表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_truck_driver")
public class PdTruckDriver implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 用户id，来自用户表
     */
    private String userId;

    /**
     * 所属车队id
     */
    private String fleetId;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 图片
     */
    private String picture;

    /**
     * 驾龄
     */
    private Integer drivingAge;


}
