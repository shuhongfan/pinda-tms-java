package com.itheima.pinda.vo.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.transforCenter.business.TransportTripsVo;
import com.itheima.pinda.vo.base.transforCenter.business.TruckVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "运单信息")
public class TaskTransportVo implements Serializable {
    private static final long serialVersionUID = 208202200909848030L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "车次")
    private TransportTripsVo transportTrips;

    @ApiModelProperty(value = "起始机构")
    private AgencySimpleVo startAgency;

    @ApiModelProperty(value = "目的机构")
    private AgencySimpleVo endAgency;

    @ApiModelProperty(value = "任务状态，1为待执行（对应 待提货）、2为进行中（对应在途）、3为待确认（保留状态）、4为已完成（对应 已交付）、5为已取消")
    private Integer status;

    @ApiModelProperty(value = "任务分配状态(1未分配2已分配3待人工分配)")
    private Integer assignedStatus;

    @ApiModelProperty(value = "满载状态(1.半载2.满载3.空载)")
    private Integer loadingStatus;

    @ApiModelProperty(value = "车辆")
    private TruckVo truck;

    @ApiModelProperty(value = "提货凭证")
    private String cargoPickUpPicture;

    @ApiModelProperty(value = "货物照片")
    private String cargoPicture;

    @ApiModelProperty(value = "运回单凭证")
    private String transportCertificate;

    @ApiModelProperty(value = "计划发车时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planDepartureTime;

    @ApiModelProperty(value = "实际发车时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualDepartureTime;

    @ApiModelProperty(value = "计划到达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planArrivalTime;

    @ApiModelProperty(value = "实际到达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualArrivalTime;

    @ApiModelProperty(value = "计划提货时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planPickUpGoodsTime;

    @ApiModelProperty(value = "实际提货时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualPickUpGoodsTime;

    @ApiModelProperty(value = "计划交付时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planDeliveryTime;

    @ApiModelProperty(value = "实际交付时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualDeliveryTime;

    @ApiModelProperty(value = "交付货物照片")
    private String deliverPicture;

    @ApiModelProperty(value = "提货纬度")
    private String deliveryLatitude;

    @ApiModelProperty(value = "提货经度")
    private String deliveryLongitude;

    @ApiModelProperty(value = "交付纬度")
    private String deliverLatitude;

    @ApiModelProperty(value = "交付经度")
    private String deliverLongitude;

    @ApiModelProperty(value = "任务创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "运单列表")
    private List<TransportOrderVo> transportOrders;

    @ApiModelProperty(value = "司机列表")
    private List<SysUserVo> drivers;

    @ApiModelProperty(value = "运单数量")
    private Integer transportOrderCount;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "页尺寸")
    private Integer pageSize;

    @ApiModelProperty(value = "司机姓名，查询条件")
    private String driverName;
}
