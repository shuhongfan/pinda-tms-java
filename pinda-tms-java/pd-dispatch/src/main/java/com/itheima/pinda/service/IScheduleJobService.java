/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.DTO.OrgJobTreeDTO;
import com.itheima.pinda.DTO.ScheduleJobDTO;
import com.itheima.pinda.entity.ScheduleJobEntity;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author
 */
public interface IScheduleJobService extends IService<ScheduleJobEntity> {

    List<OrgJobTreeDTO> page(Map<String, Object> params);

    ScheduleJobDTO get(String id);

    ScheduleJobDTO getByOrgId(String id);

    /**
     * 保存定时任务
     */
    void save(ScheduleJobDTO dto);

    /**
     * 更新定时任务
     */
    void update(ScheduleJobDTO dto);

    /**
     * 批量删除定时任务
     */
    void deleteBatch(String[] ids);

    /**
     * 批量更新定时任务状态
     */
    int updateBatch(String[] ids, int status);

    /**
     * 立即执行
     */
    void run(String[] ids);

    /**
     * 暂停运行
     */
    void pause(String[] ids);

    /**
     * 恢复运行
     */
    void resume(String[] ids);
}
