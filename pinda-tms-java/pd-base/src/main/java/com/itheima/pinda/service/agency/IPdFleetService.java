package com.itheima.pinda.service.agency;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.agency.PdFleet;

/**
 * <p>
 * 车队表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdFleetService extends IService<PdFleet> {
    /**
     * 添加车队
     *
     * @param fleet 车队信息
     * @return 车队信息
     */
    PdFleet saveFleet(PdFleet fleet);

    /**
     * 车队分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @return 车队分页数据
     */
    IPage<PdFleet> findByPage(Integer page, Integer pageSize, String name, String fleetNumber, String manager);

    /**
     * 获取车队列表
     * @param ids 车队id列表
     * @return 车队列表
     */
    List<PdFleet> findAll(List<String> ids,String agencyId);

    /**
     * 删除车队
     *
     * @param id
     */
    void disableById(String id);
}
