package com.itheima.pinda.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtils {
    /**
     * 获取utc时间
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getUTCTime(LocalDateTime localDateTime) {
        ZoneId australia = ZoneId.of("Asia/Shanghai");
        ZonedDateTime dateAndTimeInSydney = ZonedDateTime.of(localDateTime, australia);
        ZonedDateTime utcDate = dateAndTimeInSydney.withZoneSameInstant(ZoneOffset.UTC);
        return utcDate.toLocalDateTime();
    }
}
