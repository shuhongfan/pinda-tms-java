package com.itheima.pinda.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.LocationEntity;
import com.itheima.pinda.service.KafkaSender;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供Http接口方式接受司机端上报的位置信息
 */
@RestController
@RequestMapping("/netty")
@Api(tags = "车辆轨迹服务")
@Slf4j
public class NettyController {
    /**
     * 将车辆定位信息发送到kafka队列
     * @param locationEntity
     * @return
     */
    @PostMapping("/push")
    public Result push(@RequestBody LocationEntity locationEntity){
        String message = JSON.toJSONString(locationEntity);
        KafkaSender.send(KafkaSender.MSG_TOPIC,message);//将消息发送到kafka队列
        log.info("HTTP接口方式推送位置信息到kafka:{}",message);
        return Result.ok();
    }
}
