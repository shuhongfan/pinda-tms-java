package com.itheima.pinda.service.transportline.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.mapper.transportline.PdTransportLineTypeMapper;
import com.itheima.pinda.entity.transportline.PdTransportLineType;
import com.itheima.pinda.service.transportline.IPdTransportLineTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 线路类型表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTransportLineTypeServiceImpl extends ServiceImpl<PdTransportLineTypeMapper, PdTransportLineType>
        implements IPdTransportLineTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTransportLineType saveTransportLineType(PdTransportLineType pdTransportLineType) {
        pdTransportLineType.setId(idGenerator.nextId(pdTransportLineType) + "");
        pdTransportLineType.setLastUpdateTime(LocalDateTime.now());
        baseMapper.insert(pdTransportLineType);
        return pdTransportLineType;
    }

    @Override
    public IPage<PdTransportLineType> findByPage(Integer page, Integer pageSize, String typeNumber, String name,
                                                 Integer agencyType) {
        Page<PdTransportLineType> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdTransportLineType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(PdTransportLineType::getName, name);
        }
        if (StringUtils.isNotEmpty(typeNumber)) {
            lambdaQueryWrapper.like(PdTransportLineType::getTypeNumber, typeNumber);
        }
        if (agencyType != null) {
            lambdaQueryWrapper.and(i -> i.eq(PdTransportLineType::getStartAgencyType, agencyType).or()
                    .eq(PdTransportLineType::getEndAgencyType, agencyType));

        }
        lambdaQueryWrapper.eq(PdTransportLineType::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, true, PdTransportLineType::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<PdTransportLineType> findAll(List<String> ids) {
        LambdaQueryWrapper<PdTransportLineType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(PdTransportLineType::getId, ids);
        }
        lambdaQueryWrapper.orderBy(true, true, PdTransportLineType::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        PdTransportLineType pdTransportLineType = new PdTransportLineType();
        pdTransportLineType.setId(id);
        pdTransportLineType.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(pdTransportLineType);
    }

}
