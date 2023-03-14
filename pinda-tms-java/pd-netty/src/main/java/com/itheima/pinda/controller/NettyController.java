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

@RestController
@Api(tags = "车辆轨迹服务")
@RequestMapping("netty")
@Slf4j
public class NettyController {
    @PostMapping(value = "/push")
    public Result push(@RequestBody LocationEntity locationEntity) {
        String message = JSON.toJSONString(locationEntity);
        log.info("HTTP 方式推送位置信息：{}", message);

//        将消息发送到Kafka队列
        KafkaSender.send(KafkaSender.MSG_TOPIC, message);
        return Result.ok();
    }
}