package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.DriverJob;
import com.itheima.pinda.service.IDriverJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机作业单
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("driver-job")
public class DriverJobController {
    @Autowired
    private IDriverJobService driverJobService;

    /**
     * 新增司机作业单
     *
     * @param dto 司机作业单信息
     * @return 司机作业单信息
     */
    @PostMapping("")
    public DriverJobDTO save(@RequestBody DriverJobDTO dto) {
        DriverJob driverJob = new DriverJob();
        BeanUtils.copyProperties(dto, driverJob);
        driverJobService.saveDriverJob(driverJob);
        DriverJobDTO result = new DriverJobDTO();
        BeanUtils.copyProperties(driverJob, result);
        return result;
    }

    /**
     * 修改司机作业单信息
     *
     * @param id  司机作业单id
     * @param dto 司机作业单信息
     * @return 司机作业单信息
     */
    @PutMapping("/{id}")
    public DriverJobDTO updateById(@PathVariable(name = "id") String id, @RequestBody DriverJobDTO dto) {
        dto.setId(id);
        DriverJob driverJob = new DriverJob();
        BeanUtils.copyProperties(dto, driverJob);
        driverJobService.updateById(driverJob);
        return dto;
    }

    /**
     * 获取司机作业单分页数据
     *
     * @param dto 查询参数
     * @return 司机作业单分页数据
     */
    @PostMapping("/page")
    public PageResponse<DriverJobDTO> findByPage(@RequestBody DriverJobDTO dto) {
        Integer page = 1;
        Integer pageSize = 10;
        List<DriverJobDTO> dtoList = new ArrayList<>();
        Long total = 0L;
        Long pages = 0L;
        if (dto != null) {
            if (dto.getPage() != null) {
                page = dto.getPage();
            }
            if (dto.getPageSize() != null) {
                pageSize = dto.getPageSize();
            }
            IPage<DriverJob> driverJobIPage = driverJobService.findByPage(page, pageSize, dto.getId(), dto.getDriverId(), dto.getStatus(), dto.getTaskTransportId());
            driverJobIPage.getRecords().forEach(driverJob -> {
                DriverJobDTO resultDto = new DriverJobDTO();
                BeanUtils.copyProperties(driverJob, resultDto);
                dtoList.add(resultDto);
            });
            total = driverJobIPage.getTotal();
            pages = driverJobIPage.getPages();
        }
        return PageResponse.<DriverJobDTO>builder().items(dtoList).pagesize(pageSize).page(page).counts(total)
                .pages(pages).build();
    }

    /**
     * 根据id获取司机作业单信息
     *
     * @param id 司机作业单id
     * @return 司机作业单信息
     */
    @GetMapping("/{id}")
    public DriverJobDTO findById(@PathVariable(name = "id") String id) {
        DriverJobDTO dto = new DriverJobDTO();
        DriverJob driverJob = driverJobService.getById(id);
        if (driverJob != null) {
            BeanUtils.copyProperties(driverJob, dto);
        } else {
            dto = null;
        }
        return dto;
    }

    @PostMapping("/findAll")
    public List<DriverJobDTO> findAll(@RequestBody DriverJobDTO dto) {
        List<DriverJobDTO> dtos = new ArrayList<>();
        List<DriverJob> driverJobs = driverJobService.findAll(null, dto.getId(), dto.getDriverId(), dto.getStatus(), dto.getTaskTransportId());
        if (driverJobs != null) {
            for (DriverJob driverJob : driverJobs) {
                dto = new DriverJobDTO();
                BeanUtils.copyProperties(driverJob, dto);
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
