package com.itheima.pinda.execute;
import com.itheima.pinda.entity.ScheduleJobEntity;
import com.itheima.pinda.utils.ScheduleUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 定时任务
 */
public class ScheduleJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJobEntity scheduleJob =
                (ScheduleJobEntity) context.getMergedJobDataMap().
                        get(ScheduleUtils.JOB_PARAM_KEY);
        System.out.println(new Date() + "定时任务开始执行...,定时任务id：" + scheduleJob.getId());
    }
}