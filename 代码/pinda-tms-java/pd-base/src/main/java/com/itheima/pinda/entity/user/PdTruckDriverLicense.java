package com.itheima.pinda.entity.user;

import java.time.LocalDate;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 司机驾驶证表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_truck_driver_license")
public class PdTruckDriverLicense implements Serializable {
    private static final long serialVersionUID = 6323283009431070954L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 准驾车型
     */
    private String allowableType;

    /**
     * 初次领证日期
     */
    private LocalDate initialCertificateDate;

    /**
     * 有效期限
     */
    private String validPeriod;

    /**
     * 驾驶证号
     */
    private String licenseNumber;

    /**
     * 驾龄
     */
    private Integer driverAge;

    /**
     * 驾驶证类型
     */
    private String licenseType;

    /**
     * 从业资格证信息
     */
    private String qualificationCertificate;

    /**
     * 入场证信息
     */
    private String passCertificate;

    /**
     * 图片
     */
    private String picture;
}
