package com.itheima.pinda.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestContext {
    private static final String USER_ID = "userid";

    public static String getUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userid = request.getHeader(USER_ID);
        log.info("获取上下文用户id：{}", userid);
        return userid;
    }
}
