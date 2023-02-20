package com.itheima.pinda.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.vo.AgencyVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoTranTaskDTO implements Serializable {
    /**
     * 起点机构
     */
    @ApiModelProperty("起点")
    private AgencyDTO startAgency;


    /**
     * 目的机构
     */
    @ApiModelProperty("目的机构地址")
    private AgencyDTO endAgency;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;


    /**
     * 任务编号
     */
    @ApiModelProperty("任务编号")
    private String taskNo;

    /**
     * 关联运单id
     */
    @ApiModelProperty("关联运单id")
    private String tranOrderId;

    /**
     * 车次id
     */
    @ApiModelProperty("车次id")
    private String transportTripsId;

    /**
     * 车次
     */
//    @ApiModelProperty("车次")
//    private String trainNumber;

    /**
     * 运输任务状态(1.待人工调度2.待提货3.待发车4.在途6.已到达7.已交付)
     */
    @ApiModelProperty("运输任务状态(1.待人工调度2.待提货3.待发车4.在途6.已到达7.已交付)")
    private Integer status;

    /**
     * 满载状态(1.半载2.满载3.空载)
     */
    @ApiModelProperty("满载状态(1.半载2.满载3.空载)")
    private Integer loadingStatus;

    /**
     * 司机id
     */
    @ApiModelProperty("司机id")
    private String driver;

    /**
     * 车辆id
     */
    @ApiModelProperty("车辆id")
    private String truckId;

    /**
     * 运单数量
     */
    @ApiModelProperty("运单数量")
    private Integer tranOrderNum;

    /**
     * 提货凭证
     */
    @ApiModelProperty("提货凭证")
    private String cargoPickUpPicture;

    /**
     * 货物照片
     */
    @ApiModelProperty("货物照片")
    private String cargoPicture;

    /**
     * 运回单凭证
     */
    @ApiModelProperty("运回单凭证")
    private String transportCertificate;
    /**
     * 货物照片
     */
    @ApiModelProperty("货物照片")
    private String deliverPicture;

    /**
     * 计划发车时间
     */
    @ApiModelProperty("计划发车时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planDepartureTime;

    /**
     * 实际发车时间
     */
    @ApiModelProperty("实际发车时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualDepartureTime;

    /**
     * 计划到达时间
     */
    @ApiModelProperty("计划到达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planArrivalTime;

    /**
     * 实际到达时间
     */
    @ApiModelProperty("实际到达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualArrivalTime;

    /**
     * 计划提货时间
     */
    @ApiModelProperty("计划提货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planPickUpGoodsTime;

    /**
     * 实际提货时间
     */
    @ApiModelProperty("实际提货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualPickUpGoodsTime;

    /**
     * 计划交付时间
     */
    @ApiModelProperty("计划交付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planDeliveryTime;

    /**
     * 实际交付时间
     */
    @ApiModelProperty("实际交付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualDeliveryTime;

    @ApiModelProperty("是否显示")
    private boolean disable;

    public CargoTranTaskDTO(DriverJobDTO item, Map<String, TaskTransportDTO> taskTransportDTOMap, Map agencyMap) {
        AgencyVo agencyVoStart = (AgencyVo) agencyMap.get(item.getStartAgencyId());
        AgencyVo agencyVoEnd = (AgencyVo) agencyMap.get(item.getEndAgencyId());
        TaskTransportDTO taskTransportDTO = taskTransportDTOMap.get(item.getTaskTransportId());
        this.cargoPickUpPicture = taskTransportDTO.getCargoPickUpPicture();
        this.cargoPicture = taskTransportDTO.getCargoPicture();
        this.transportCertificate = taskTransportDTO.getTransportCertificate();
        this.deliverPicture = taskTransportDTO.getDeliverPicture();
        this.taskNo = taskTransportDTO.getId();
        this.transportTripsId = taskTransportDTO.getTransportTripsId();
        this.truckId = taskTransportDTO.getTruckId();
        this.planPickUpGoodsTime = taskTransportDTO.getPlanPickUpGoodsTime();
        this.actualPickUpGoodsTime = taskTransportDTO.getActualPickUpGoodsTime();
        this.planDeliveryTime = taskTransportDTO.getPlanDeliveryTime();
        this.actualDeliveryTime = taskTransportDTO.getActualDeliveryTime();
        this.planDepartureTime = taskTransportDTO.getPlanDepartureTime();
        this.tranOrderNum = taskTransportDTO.getTransportOrderCount();

        this.driver = item.getDriverId();

        this.status = item.getStatus();
        this.id = item.getId();
        this.actualDepartureTime = item.getActualDepartureTime();
        this.planArrivalTime = item.getPlanArrivalTime();
        this.actualArrivalTime = item.getActualArrivalTime();

        this.startAgency = new AgencyDTO(agencyVoStart);
        this.endAgency = new AgencyDTO(agencyVoEnd);
    }
}
