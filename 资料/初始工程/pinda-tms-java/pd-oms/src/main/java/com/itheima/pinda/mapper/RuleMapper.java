package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.entity.Rule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规则
 */
@Mapper
public interface RuleMapper extends BaseMapper<Rule> {
}