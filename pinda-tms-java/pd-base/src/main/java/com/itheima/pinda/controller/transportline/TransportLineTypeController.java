package com.itheima.pinda.controller.transportline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.transportline.PdTransportLineType;
import com.itheima.pinda.service.transportline.IPdTransportLineTypeService;
import com.itheima.pinda.DTO.transportline.TransportLineTypeDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TransportLineTypeController
 */
@RestController
@RequestMapping("base/transportLine/type")
public class TransportLineTypeController {
    @Autowired
    private IPdTransportLineTypeService transportLineTypeService;

    /**
     * 添加线路类型
     *
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PostMapping("")
    public TransportLineTypeDto saveTransportLineType(@RequestBody TransportLineTypeDto dto) {
        PdTransportLineType pdTransportLineType = new PdTransportLineType();
        BeanUtils.copyProperties(dto, pdTransportLineType);
        pdTransportLineType = transportLineTypeService.saveTransportLineType(pdTransportLineType);
        BeanUtils.copyProperties(pdTransportLineType, dto);
        return dto;
    }

    /**
     * 根据id获取线路类型详情
     *
     * @param id 线路类型id
     * @return 线路类型详情
     */
    @GetMapping("/{id}")
    public TransportLineTypeDto fineById(@PathVariable(name = "id") String id) {
        PdTransportLineType pdTransportLineType = transportLineTypeService.getById(id);
        TransportLineTypeDto dto = new TransportLineTypeDto();
        BeanUtils.copyProperties(pdTransportLineType, dto);
        return dto;
    }

    /**
     * 获取线路类型分页数据
     *
     * @param page       页码
     * @param pageSize   页尺寸
     * @param typeNumber 类型编号
     * @param name       类型名称
     * @param agencyType 机构类型
     * @return 线路类型分页数据
     */
    @GetMapping("/page")
    public PageResponse<TransportLineTypeDto> findByPage(@RequestParam(name = "page") Integer page,
                                                         @RequestParam(name = "pageSize") Integer pageSize,
                                                         @RequestParam(name = "typeNumber", required = false) String typeNumber,
                                                         @RequestParam(name = "name", required = false) String name,
                                                         @RequestParam(name = "agencyType", required = false) Integer agencyType) {
        IPage<PdTransportLineType> transportLineTypePage = transportLineTypeService.findByPage(page, pageSize, typeNumber, name, agencyType);
        List<TransportLineTypeDto> dtoList = new ArrayList<>();
        transportLineTypePage.getRecords().forEach(pdTransportLineType -> {
            TransportLineTypeDto dto = new TransportLineTypeDto();
            BeanUtils.copyProperties(pdTransportLineType, dto);
            dtoList.add(dto);
        });
        return PageResponse.<TransportLineTypeDto>builder().items(dtoList).pagesize(pageSize).page(page)
                .counts(transportLineTypePage.getTotal()).pages(transportLineTypePage.getPages()).build();
    }

    /**
     * 获取线路类型列表
     *
     * @param ids 线路类型id列表
     * @return 线路类型列表
     */
    @GetMapping("")
    public List<TransportLineTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        return transportLineTypeService.findAll(ids).stream().map(pdTransportLineType -> {
            TransportLineTypeDto dto = new TransportLineTypeDto();
            BeanUtils.copyProperties(pdTransportLineType, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新线路类型信息
     *
     * @param id  线路类型id
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PutMapping("/{id}")
    public TransportLineTypeDto update(@PathVariable(name = "id") String id, @RequestBody TransportLineTypeDto dto) {
        dto.setId(id);
        PdTransportLineType pdTransportLineType = new PdTransportLineType();
        BeanUtils.copyProperties(dto, pdTransportLineType);
        pdTransportLineType.setLastUpdateTime(LocalDateTime.now());
        transportLineTypeService.updateById(pdTransportLineType);
        return dto;
    }

    /**
     * 删除线路类型
     *
     * @param id 线路类型id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    public Result disable(@PathVariable(name = "id") String id) {
        transportLineTypeService.disableById(id);
        return Result.ok();
    }
}
