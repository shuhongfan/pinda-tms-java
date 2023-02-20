package com.itheima.pinda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 用于操作Kafka
 */
@Component
public class KafkaSender {
    public final static String MSG_TOPIC = "tms_order_location";//kafka队列名称

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private static KafkaTemplate<String,String> template;

    @PostConstruct
    public void init(){
        template = kafkaTemplate;
    }

    /**
     * 向kafka队列发送消息
     */
    public static boolean send(String topic,String message){
        try {
            template.send(topic,message);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
