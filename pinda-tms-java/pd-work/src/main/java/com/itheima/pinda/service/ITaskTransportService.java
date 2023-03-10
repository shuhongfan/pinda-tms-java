package com.itheima.pinda.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.DTO.TaskTransportDTO;
import com.itheima.pinda.entity.TaskTransport;

import java.util.List;

/**
 * <p>
 * 运输任务表 服务类
 * </p>
 */
public interface ITaskTransportService extends IService<TaskTransport> {
    /**
     * 新增运输任务
     *
     * @param taskTransport 运输任务信息
     * @return 运输任务信息
     */
    TaskTransport saveTaskTransport(TaskTransport taskTransport);

    /**
     * 获取运输任务分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param id       任务id
     * @param status   运输任务状态
     * @return 运输任务分页数据
     */
    IPage<TaskTransport> findByPage(Integer page, Integer pageSize, String id, Integer status);

    /**
     * 获取运输任务列表
     *
     * @param ids    运输任务id列表
     * @param id     运输任务Id
     * @param status 运单状态
     * @param dto
     * @return 运输任务列表
     */
    List<TaskTransport> findAll(List<String> ids, String id, Integer status, TaskTransportDTO dto);
}
