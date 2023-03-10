/**
 * Copyright (c) 2019 联智合创 All rights reserved.
 * <p>
 * http://www.witlinked.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.entity.CacheLineEntity;
import com.itheima.pinda.mapper.CacheLineMapper;
import com.itheima.pinda.service.ICacheLineService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CacheLineServiceImpl extends ServiceImpl<CacheLineMapper, CacheLineEntity> implements ICacheLineService {

    @Override
    public String check(String verifyKey) {
        LambdaQueryWrapper<CacheLineEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CacheLineEntity::getVerifyKey, verifyKey);
        wrapper.eq(CacheLineEntity::getIsCurrent, 1);
        CacheLineEntity result = baseMapper.selectOne(wrapper);
        if (result == null) {
            return null;
        }
        return result.getId();
    }

    @Override
    public int saveAndUpdate(CacheLineEntity cacheLineEntity) {
        // 查询当前最大version
        Integer version = 1;
        LambdaQueryWrapper<CacheLineEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CacheLineEntity::getIsCurrent, 1);
        queryWrapper.eq(CacheLineEntity::getStartAgencyId, cacheLineEntity.getStartAgencyId());
        queryWrapper.eq(CacheLineEntity::getEndAgencyId, cacheLineEntity.getEndAgencyId());
        CacheLineEntity result = baseMapper.selectOne(queryWrapper);
        if (result != null) {
            version = result.getVersion();
            version++;
        }

        // 修改当前所有 当前版本为 false
        CacheLineEntity cacheLineEntityUpdate = new CacheLineEntity();
        cacheLineEntityUpdate.setIsCurrent(0);
        LambdaUpdateWrapper<CacheLineEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CacheLineEntity::getStartAgencyId, cacheLineEntity.getStartAgencyId());
        updateWrapper.eq(CacheLineEntity::getEndAgencyId, cacheLineEntity.getEndAgencyId());
        updateWrapper.eq(CacheLineEntity::getVersion, version - 1);
        baseMapper.update(cacheLineEntityUpdate, updateWrapper);

        // 保存当前版本
        cacheLineEntity.setVersion(version);
        return baseMapper.insert(cacheLineEntity);

    }

    @Override
    public Integer deleteOldAndGetNewVersion(String notEqualsLineStart, String notEqualsLineEnd) {
        // 查询当前最大version
        Integer version = 0;
        LambdaQueryWrapper<CacheLineEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CacheLineEntity::getIsCurrent, 1);
        queryWrapper.eq(CacheLineEntity::getStartAgencyId, notEqualsLineStart);
        queryWrapper.eq(CacheLineEntity::getEndAgencyId, notEqualsLineEnd);
        List<CacheLineEntity> results = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(results)) {
            version = results.get(0).getVersion();

            // 修改当前所有 当前版本为 false
            CacheLineEntity cacheLineEntityUpdate = new CacheLineEntity();
            cacheLineEntityUpdate.setIsCurrent(0);
            LambdaUpdateWrapper<CacheLineEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(CacheLineEntity::getStartAgencyId, notEqualsLineStart);
            updateWrapper.eq(CacheLineEntity::getEndAgencyId, notEqualsLineEnd);
            updateWrapper.eq(CacheLineEntity::getIsCurrent, 1);
            baseMapper.update(cacheLineEntityUpdate, updateWrapper);
        }

        return ++version;
    }
}