package com.itheima.pinda.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.base.PdGoodsType;

import java.util.List;

/**
 * 货物类型操作接口
 */
public interface IPdGoodsTypeService extends IService<PdGoodsType>{
    /**
     * 保存货物类型
     * @param pdGoodsType
     * @return
     */
    public PdGoodsType saveGoodsType(PdGoodsType pdGoodsType);

    /**
     * 查询所有货物类型
     * @return
     */
    public List<PdGoodsType> findAll();

    /**
     * 分页查询货物类型
     * @param page
     * @param pageSize
     * @param name
     * @param truckTypeId
     * @param truckTypeName
     * @return
     */
    public IPage<PdGoodsType> findByPage(Integer page, Integer pageSize,String name,String truckTypeId,String truckTypeName);

    /**
     * 查询货物类型列表
     * @param ids
     * @return
     */
    public List<PdGoodsType> findAll(List<String> ids);
}
