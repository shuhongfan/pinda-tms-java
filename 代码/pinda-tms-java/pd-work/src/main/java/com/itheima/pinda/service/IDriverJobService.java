package com.itheima.pinda.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.DriverJob;
import com.itheima.pinda.entity.TaskTransport;

import java.util.List;

/**
 * <p>
 * 运输任务表 服务类
 * </p>
 */
public interface IDriverJobService extends IService<DriverJob> {
    /**
     * 新增司机作业单
     *
     * @param driverJob 作业单信息
     * @return 作业单信息
     */
    DriverJob saveDriverJob(DriverJob driverJob);

    /**
     * 获取司机作业单分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param id 作业Id
     * @param driverId 司机id
     * @param status 作业状态
     * @param taskTransportId 运输任务id
     * @return 司机作业单分页数据
     */
    IPage<DriverJob> findByPage(Integer page, Integer pageSize,String id,String driverId,Integer status,String taskTransportId);

    /**
     * 获取司机作业单列表
     *
     * @param ids    司机作业单id列表
     * @param id     司机作业单Id
     * @param status 司机作业单状态
     * @return 司机作业单列表
     */
    List<DriverJob> findAll(List<String> ids, String id, String driverId, Integer status, String taskTransportId);
}
