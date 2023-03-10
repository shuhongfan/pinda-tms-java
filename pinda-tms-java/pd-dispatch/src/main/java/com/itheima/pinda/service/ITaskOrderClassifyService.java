package com.itheima.pinda.service;

import com.itheima.pinda.DTO.OrderClassifyGroupDTO;

import java.util.List;

/**
 * 处理订单分类Service接口
 */
public interface ITaskOrderClassifyService {
    /**
     * 订单分类
     * @param agencyId 机构id（网点或者转运中心的id）
     * @param jobId 定时任务id
     * @param logId 日志id
     * @return
     */
    List<OrderClassifyGroupDTO> execute(String agencyId, String jobId, String logId);
}
