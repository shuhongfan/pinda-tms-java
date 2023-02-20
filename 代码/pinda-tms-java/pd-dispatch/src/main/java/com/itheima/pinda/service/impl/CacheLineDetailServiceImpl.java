/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.entity.CacheLineDetailEntity;
import com.itheima.pinda.mapper.CacheLineDetailMapper;
import com.itheima.pinda.service.ICacheLineDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheLineDetailServiceImpl extends ServiceImpl<CacheLineDetailMapper, CacheLineDetailEntity> implements ICacheLineDetailService {

    @Override
    public List<CacheLineDetailEntity> findByCacheLineId(String cacheLineId) {
        LambdaQueryWrapper<CacheLineDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CacheLineDetailEntity::getCacheLineId, cacheLineId);
        return this.baseMapper.selectList(wrapper);
    }
}