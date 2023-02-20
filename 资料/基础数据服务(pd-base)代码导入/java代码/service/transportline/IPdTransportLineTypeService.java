package com.itheima.pinda.service.transportline;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.transportline.PdTransportLineType;

import java.util.List;

/**
 * <p>
 * 线路类型表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTransportLineTypeService extends IService<PdTransportLineType> {
    /**
     * 添加线路类型
     *
     * @param pdTransportLineType 线路类型信息
     * @return 线路类型信息
     */
    PdTransportLineType saveTransportLineType(PdTransportLineType pdTransportLineType);

    /**
     * 获取线路类型分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param typeNumber   类型编号
     * @param name         类型名称
     * @param agencyType 机构类型
     * @return 线路类型分页数据
     */
    IPage<PdTransportLineType> findByPage(Integer page, Integer pageSize, String typeNumber, String name, Integer agencyType);

    /**
     * 获取线路类型列表
     *
     * @param ids 线路类型id列表
     * @return 线路类型列表
     */
    List<PdTransportLineType> findAll(List<String> ids);

    /**
     * 删除线路类型
     *
     * @param id
     */
    void disableById(String id);
}
