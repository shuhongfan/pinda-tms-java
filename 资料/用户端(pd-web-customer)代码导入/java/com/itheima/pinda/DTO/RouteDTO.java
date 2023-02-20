package com.itheima.pinda.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class RouteDTO {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    //信息
    private String msg;

    //时间
    private LocalDateTime time;
    //状态 类型 已揽收 待揽收 已取消 运送中 派送中 已签收.....
    private int status;

    private int quantity;

    public String getDateStr(){
        if (time != null) {
            return dateFormatter.format(time);
        }
        return "";
    }
    public String getTimeStr(){
        if (time != null) {
            return timeFormatter.format(time);
        }
        return "";
    }

}
