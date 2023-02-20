package com.itheima.pinda.config;

import com.itheima.pinda.common.filter.TxXidFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApacheDruid {

    @Bean
    public FilterRegistrationBean cancelMyFilterRegistration(TxXidFilter filter) {
        log.info(">>>>>>>>>>> 取消TxXidFilter 注册");
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}

