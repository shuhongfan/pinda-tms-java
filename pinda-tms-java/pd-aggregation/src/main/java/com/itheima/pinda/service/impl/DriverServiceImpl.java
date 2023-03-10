package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.DTO.AppDriverQueryDTO;
import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.mapper.DriverMapper;
import com.itheima.pinda.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverMapper driverMapper;

    @Override
    public PageResponse<DriverJobDTO> findByPage(AppDriverQueryDTO dto) {
        IPage<DriverJobDTO> iPage = new Page();
        iPage.setSize(dto.getPageSize());
        iPage.setCurrent(dto.getPage());
        driverMapper.findByPage(iPage, dto);

        return PageResponse.<DriverJobDTO>builder()
                .counts(iPage.getTotal())
                .pages(iPage.getPages())
                .pagesize(dto.getPageSize())
                .page(dto.getPage())
                .items(iPage.getRecords())
                .build();
    }
}
