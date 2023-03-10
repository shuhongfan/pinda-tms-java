package com.itheima.pinda.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.future.PdCompletableFuture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 网点  前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-3-30
 */
@Log4j2
@Api(tags = "网点自寄")
@RestController
@RequestMapping("agency")
public class AgencyController {

    private final OrgApi orgApi;

    private final AreaApi areaApi;

    public AgencyController(OrgApi orgApi, AreaApi areaApi) {
        this.orgApi = orgApi;
        this.areaApi = areaApi;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pagesize
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "网点自寄分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, example = "1"),
            @ApiImplicitParam(name = "pagesize", value = "每页条数", required = true, example = "10"),
            @ApiImplicitParam(name = "cityId", value = "市id", required = false, example = ""),
            @ApiImplicitParam(name = "keyword", value = "搜索条件", required = false, example = ""),
            @ApiImplicitParam(name = "latitude", value = "维度", required = false, example = ""),
            @ApiImplicitParam(name = "longitude", value = "精度", required = false, example = "")
    })
    @GetMapping("page")
    public Result page(Integer page, Integer pagesize, Long cityId, String keyword, String latitude, String longitude) {

        R<Page> result = orgApi.pageLike(pagesize, page, keyword, cityId, latitude, longitude);
        Page pageResult = result.getData();
        List<Map> records = pageResult.getRecords();

        Set<Long> areaSet = new HashSet<>();
        areaSet.addAll(records.stream().filter(item -> !ObjectUtils.isEmpty(item.get("provinceId"))).map(item -> Long.valueOf(item.get("provinceId").toString())).collect(Collectors.toSet()));
        areaSet.addAll(records.stream().filter(item -> !ObjectUtils.isEmpty(item.get("cityId"))).map(item -> Long.valueOf(item.get("cityId").toString())).collect(Collectors.toSet()));
        areaSet.addAll(records.stream().filter(item -> !ObjectUtils.isEmpty(item.get("countyId"))).map(item -> Long.valueOf(item.get("countyId").toString())).collect(Collectors.toSet()));
        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = areaMapFuture.get();

        records = records.stream().map(item -> {
            Map newItem = writeMapNullToEmpty(item);
            newItem.put("province", (item.get("provinceId") != null && areaMap.get(Long.valueOf(item.get("provinceId").toString())) != null) ? areaMap.get(Long.valueOf(item.get("provinceId").toString())).getName() : "");
            newItem.put("city", (item.get("cityId") != null && areaMap.get(Long.valueOf(item.get("cityId").toString())) != null) ? areaMap.get(Long.valueOf(item.get("cityId").toString())).getName() : "");
            newItem.put("county", (item.get("countyId") != null && areaMap.get(Long.valueOf(item.get("countyId").toString())) != null) ? areaMap.get(Long.valueOf(item.get("countyId").toString())).getName() : "");
            newItem.put("fullAddress", (String) newItem.get("province") + newItem.get("city") + newItem.get("county") + newItem.get("address"));
            return newItem;
        }).collect(Collectors.toList());

        return Result.ok().put("data",PageResponse.<Map>builder()
                .items(records)
                .pagesize(pagesize)
                .page(page)
                .pages(pageResult.getPages())
                .counts(pageResult.getTotal())
                .build());
    }

    private Map writeMapNullToEmpty(Map params) {
        Map map = new HashMap();
        params.keySet().forEach(key -> {
            if (params.get(key) != null && !StringUtils.isEmpty(params.get(key))) {
                map.put(key, params.get(key));
            } else {
                map.put(key, "");
            }
        });
        return map;
    }
}
