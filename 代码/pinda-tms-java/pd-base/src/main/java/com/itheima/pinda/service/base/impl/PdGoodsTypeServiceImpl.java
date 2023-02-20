package com.itheima.pinda.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

/**
 * 货物类型管理服务接口实现
 */
@Service
public class PdGoodsTypeServiceImpl extends ServiceImpl<PdGoodsTypeMapper,PdGoodsType> implements IPdGoodsTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;
    /**
     * 保存货物类型
     * @param pdGoodsType
     * @return
     */
    @Override
    public PdGoodsType saveGoodsType(PdGoodsType pdGoodsType) {
        pdGoodsType.setId(idGenerator.nextId(pdGoodsType) + "");
        baseMapper.insert(pdGoodsType);
        return pdGoodsType;
    }

    /**
     * 查询所有货物类型
     * @return
     */
    @Override
    public List<PdGoodsType> findAll() {
        QueryWrapper<PdGoodsType> queryWrapper = new QueryWrapper<>();
        //添加条件，根据status查询
        queryWrapper.eq("status",1);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询货物类型
     * @param page
     * @param pageSize
     * @param name
     * @param truckTypeId
     * @param truckTypeName
     * @return
     */
    @Override
    public IPage<PdGoodsType> findByPage(Integer page, Integer pageSize, String name, String truckTypeId, String truckTypeName) {
        //封装分页条件
        Page<PdGoodsType> pdPage = new Page<>(page,pageSize);
        //排序条件
        pdPage.addOrder(OrderItem.asc("id"));
        pdPage.setRecords(baseMapper.findByPage(pdPage,name,truckTypeId,truckTypeName));
        return pdPage;
    }

    /**
     * 查询货物类型列表
     * @param ids
     * @return
     */
    @Override
    public List<PdGoodsType> findAll(List<String> ids) {
        LambdaQueryWrapper<PdGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(ids != null && ids.size() > 0){
            //添加查询条件
            lambdaQueryWrapper.in(PdGoodsType::getId,ids);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
