package com.itheima.pinda.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.base.PdGoodsType;
import com.itheima.pinda.mapper.base.PdGoodsTypeMapper;
import com.itheima.pinda.service.base.IPdGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询所有货物类型
     * @return
     */
    @Override
    public List<PdGoodsType> findAll() {
        LambdaQueryWrapper<PdGoodsType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PdGoodsType::getStatus, 1);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 获取分页货物类型数据
     *
     * @param page          页码
     * @param pageSize      页尺寸
     * @param name          货物类型名称
     * @param truckTypeId   车辆类型Id
     * @param truckTypeName 车辆类型名称
     * @return
     */
    @Override
    public IPage<PdGoodsType> findByPage(Integer page, Integer pageSize, String name, String truckTypeId, String truckTypeName) {
//        封装分页条件
        Page<PdGoodsType> pdGoodsTypePage = new Page<>(page, pageSize);

//        排序条件
        pdGoodsTypePage.addOrder(OrderItem.asc("id"));
        pdGoodsTypePage.setRecords(baseMapper.findByPage(pdGoodsTypePage, name, truckTypeId, truckTypeName));

        return pdGoodsTypePage;
    }

    /**
     * 查询所有货物类型
     * @param ids
     * @return
     */
    @Override
    public List<PdGoodsType> findAll(List<String> ids) {
        LambdaQueryWrapper<PdGoodsType> wrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            wrapper.in(PdGoodsType::getId, ids);
        }
        return baseMapper.selectList(wrapper);
    }




}
