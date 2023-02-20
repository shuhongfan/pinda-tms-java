package com.itheima.pinda.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.user.PdTruckDriverLicense;

/**
 * <p>
 * 司机驾驶证表  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTruckDriverLicenseService extends IService<PdTruckDriverLicense> {
    /**
     * 保存司机驾驶证信息
     *
     * @param pdTruckDriverLicense 司机驾驶证信息
     * @return 司机驾驶证信息
     */
    PdTruckDriverLicense saveTruckDriverLicense(PdTruckDriverLicense pdTruckDriverLicense);

    /**
     * 获取司机驾驶证信息
     *
     * @param userId 司机id
     * @return 司机驾驶证信息
     */
    PdTruckDriverLicense findOne(String userId);
}
