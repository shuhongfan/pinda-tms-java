package com.itheima.pinda.init;

import com.itheima.pinda.entity.ScheduleJobEntity;
import com.itheima.pinda.mapper.ScheduleJobMapper;
import com.itheima.pinda.utils.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在项目启动时初始化定时任务
 */
@Component
@Slf4j
public class DispatchCommandLineRunner implements CommandLineRunner {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始进行定时任务初始化...");
        //查询定时任务表schedule_job中所有的数据
        List<ScheduleJobEntity> list = scheduleJobMapper.selectList(null);
        for (ScheduleJobEntity scheduleJobEntity : list) {
            //获得触发器对象
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJobEntity.getId());
            if(cronTrigger == null){
                //触发器对象为空，说明对应的定时任务还没有创建
                ScheduleUtils.createScheduleJob(scheduler,scheduleJobEntity);
            }else{
                //触发器对象不为空，说明对应的定时任务已经存在了，此时只需要更新
                ScheduleUtils.updateScheduleJob(scheduler,scheduleJobEntity);
            }
        }
    }
}
