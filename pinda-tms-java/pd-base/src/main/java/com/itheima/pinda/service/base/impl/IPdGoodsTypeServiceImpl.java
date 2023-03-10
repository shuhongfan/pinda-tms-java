package com.itheima.pinda.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.base.PdGoodsType;
import com.itheima.pinda.mapper.base.PdGoodsTypeMapper;
import com.itheima.pinda.service.base.IPdGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPdGoodsTypeServiceImpl extends ServiceImpl<PdGoodsTypeMapper,PdGoodsType> implements IPdGoodsTypeService {

    @Autowired
    private CustomIdGenerator idGenerator;

    /**
     * 添加货物类型
     *
     * @param pdGoodsType 货物类型信息
     * @return 货物类型信息
     */
    @Override
    public PdGoodsType saveGoodsType(PdGoodsType pdGoodsType) {
        pdGoodsType.setId(String.valueOf(idGenerator.nextId(pdGoodsType)));
        baseMapper.insert(pdGoodsType);
        return pdGoodsType;
    }


}
