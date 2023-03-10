package com.itheima.pinda.common;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.pinda.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 自定义ID生成器
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
    }
    
    @Autowired
    private IdWorker idWorker;

    @Override
    public Long nextId(Object entity) {
        return idWorker.nextId();
    }

}
