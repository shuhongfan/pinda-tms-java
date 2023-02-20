/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.DTO;

import com.itheima.pinda.entity.ScheduleJobEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 定时任务
 *
 * @author
 * @since 1.0.0
 */
@Data
@ApiModel(value = "定时任务")
public class ScheduleJobDTO extends ScheduleJobEntity {

}
