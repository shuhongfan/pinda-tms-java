package com.itheima.pinda.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.user.PdCourierScope;

import java.util.List;

/**
 * <p>
 * 快递员业务范围表  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdCourierScopeService extends IService<PdCourierScope> {
    /**
     * 批量保存快递员业务范围
     *
     * @param scopeList 快递员业务范围信息列表
     */
    void batchSave(List<PdCourierScope> scopeList);

    /**
     * 删除快递员业务范围
     *
     * @param areaId 行政区域id
     * @param userId 快递员id
     */
    void delete(String areaId, String userId);

    List<PdCourierScope> findAll(String areaId, String userId);
}
