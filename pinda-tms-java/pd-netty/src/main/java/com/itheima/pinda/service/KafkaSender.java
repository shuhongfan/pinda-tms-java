package com.itheima.pinda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class KafkaSender {

    public final static String MSG_TOPIC = "tms_order_location";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static KafkaTemplate<String, String> template;

    @PostConstruct
    public void init() {
        template = kafkaTemplate;
    }

    /**
     * 向kafka队列发送消息
     */
    public static boolean send(String topid, String message) {
        try {
            template.send(topid, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
