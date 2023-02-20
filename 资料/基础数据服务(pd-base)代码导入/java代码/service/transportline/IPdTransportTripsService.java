package com.itheima.pinda.service.transportline;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.transportline.PdTransportTrips;

import java.util.List;

/**
 * <p>
 * 车次信息表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTransportTripsService extends IService<PdTransportTrips> {
    /**
     * 添加车次
     *
     * @param pdTransportTrips 车次信息
     * @return 车次信息
     */
    PdTransportTrips saveTransportTrips(PdTransportTrips pdTransportTrips);

    /**
     * 获取车次列表
     *
     * @param transportLineId 线路id
     * @param ids             车次id列表
     * @return 车次列表
     */
    List<PdTransportTrips> findAll(String transportLineId, List<String> ids);

    /**
     * 删除车次
     *
     * @param id
     */
    void disable(String id);
}
