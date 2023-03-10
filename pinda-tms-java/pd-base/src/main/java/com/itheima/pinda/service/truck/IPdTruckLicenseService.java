package com.itheima.pinda.service.truck;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.truck.PdTruckLicense;

/**
 * <p>
 * 车辆行驶证表  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTruckLicenseService extends IService<PdTruckLicense> {
    /**
     * 保存车辆行驶证信息
     *
     * @param pdTruckLicense 车辆行驶证信息
     * @return 车辆行驶证信息
     */
    PdTruckLicense saveTruckLicense(PdTruckLicense pdTruckLicense);
}
