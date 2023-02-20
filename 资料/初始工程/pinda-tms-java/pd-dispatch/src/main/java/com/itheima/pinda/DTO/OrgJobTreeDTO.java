package com.itheima.pinda.DTO;

import com.itheima.pinda.authority.dto.core.OrgTreeDTO;
import lombok.Data;

@Data
public class OrgJobTreeDTO extends OrgTreeDTO {

    /**
     * jobId
     */
    private String jobId;
    /**
     * 参数
     */
    private String params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 任务状态  0：暂停  1：正常
     */
    private Integer jobStatus;
    /**
     * 备注
     */
    private String remark;
}
