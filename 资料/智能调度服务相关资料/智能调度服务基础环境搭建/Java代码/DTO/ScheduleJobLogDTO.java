/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.DTO;

import com.itheima.pinda.entity.ScheduleJobLogEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 定时任务日志
 *
 * @author
 * @since 1.0.0
 */
@Data
@ApiModel(value = "定时任务日志")
public class ScheduleJobLogDTO extends ScheduleJobLogEntity {

    List<OrderClassifyLogDTO> orderClassifyLogDTOS;
}
