package com.itheima.pinda.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.user.PdTruckDriver;

import java.util.List;

/**
 * <p>
 * 司机表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTruckDriverService extends IService<PdTruckDriver> {
    /**
     * 添加司机
     *
     * @param pdTruckDriver 司机信息
     * @return 司机信息
     */
    PdTruckDriver saveTruckDriver(PdTruckDriver pdTruckDriver);

    /**
     * 获取司机基本信息列表
     *
     * @param userIds 司机id列表
     * @return 司机基本信息列表
     */
    List<PdTruckDriver> findAll(List<String> userIds,String fleetId);

    /**
     * 获取司机基本信息
     *
     * @param userId 司机id
     * @return 司机基本信息
     */
    PdTruckDriver findOne(String userId);


    /**
     * 统计司机数量
     * @param fleetId 车队id
     * @return 司机数量
     */
    Integer count(String fleetId);

    /**
     * 获取司机分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param fleetId  车队id
     * @return 司机分页数据
     */
    IPage<PdTruckDriver> findByPage(Integer page, Integer pageSize, String fleetId);
}
