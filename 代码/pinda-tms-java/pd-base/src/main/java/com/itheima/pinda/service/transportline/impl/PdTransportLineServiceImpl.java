package com.itheima.pinda.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.mapper.transportline.PdTransportLineMapper;
import com.itheima.pinda.entity.transportline.PdTransportLine;
import com.itheima.pinda.service.transportline.IPdTransportLineService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 线路表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTransportLineServiceImpl extends ServiceImpl<PdTransportLineMapper, PdTransportLine>
        implements IPdTransportLineService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTransportLine saveTransportLine(PdTransportLine pdTransportLine) {
        pdTransportLine.setId(idGenerator.nextId(pdTransportLine) + "");
        baseMapper.insert(pdTransportLine);
        return pdTransportLine;
    }

    @Override
    public IPage<PdTransportLine> findByPage(Integer page, Integer pageSize, String lineNumber, String name,
                                             String transportLineTypeId) {
        Page<PdTransportLine> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdTransportLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(PdTransportLine::getName, name);
        }
        if (StringUtils.isNotEmpty(lineNumber)) {
            lambdaQueryWrapper.like(PdTransportLine::getLineNumber, lineNumber);
        }
        if (StringUtils.isNotEmpty(transportLineTypeId)) {
            lambdaQueryWrapper.eq(PdTransportLine::getTransportLineTypeId, transportLineTypeId);

        }
        lambdaQueryWrapper.eq(PdTransportLine::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, PdTransportLine::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<PdTransportLine> findAll(List<String> ids, String agencyId, List<String> agencyIds) {
        LambdaQueryWrapper<PdTransportLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(PdTransportLine::getId, ids);
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(PdTransportLine::getAgencyId, agencyId);
        }
        if (agencyIds != null && agencyIds.size() > 0) {
            lambdaQueryWrapper.in(PdTransportLine::getAgencyId, agencyIds);
        }
        lambdaQueryWrapper.eq(PdTransportLine::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, PdTransportLine::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disable(String id) {
        PdTransportLine pdTransportLine = new PdTransportLine();
        pdTransportLine.setId(id);
        pdTransportLine.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(pdTransportLine);
    }

}
