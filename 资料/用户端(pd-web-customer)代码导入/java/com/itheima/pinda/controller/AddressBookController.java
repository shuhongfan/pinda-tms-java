package com.itheima.pinda.controller;


import com.itheima.pinda.DTO.AddressBookDTO;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.common.context.RequestContext;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.AddressBook;
import com.itheima.pinda.feign.AddressBookFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 地址簿  前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-3-30
 */
@Log4j2
@Api(tags = "地址簿")
@RestController
@RequestMapping("address")
public class AddressBookController {

    private final AddressBookFeign addressBookFeign;

    private final AreaApi areaApi;

    public AddressBookController(AddressBookFeign addressBookFeign, AreaApi areaApi) {
        this.addressBookFeign = addressBookFeign;
        this.areaApi = areaApi;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "地址簿分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, example = "10"),
            @ApiImplicitParam(name = "keyword", value = "搜索条件", required = false, example = "")
    })
    @GetMapping("page")
    public Result page(Integer page, Integer pageSize, String keyword) {
        //获取userid
        String userId = RequestContext.getUserId();
        PageResponse<AddressBook> result = addressBookFeign.page(page, pageSize, userId, keyword);
        Set<Long> areaSet = new HashSet<>();
        areaSet.addAll(result.getItems().stream().map(item -> item.getProvinceId()).collect(Collectors.toSet()));
        areaSet.addAll(result.getItems().stream().map(item -> item.getCityId()).collect(Collectors.toSet()));
        areaSet.addAll(result.getItems().stream().map(item -> item.getCountyId()).collect(Collectors.toSet()));
        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = areaMapFuture.get();

        List<AddressBookDTO> newItems = result.getItems().stream().map(item -> {
            AddressBookDTO addressBookDTO = new AddressBookDTO();
            BeanUtils.copyProperties(item, addressBookDTO);
            addressBookDTO.setProvince(areaMap.containsKey(addressBookDTO.getProvinceId()) ? areaMap.get(addressBookDTO.getProvinceId()).getName() : "");
            addressBookDTO.setCity(areaMap.containsKey(addressBookDTO.getCityId()) ? areaMap.get(addressBookDTO.getCityId()).getName() : "");
            addressBookDTO.setCounty(areaMap.containsKey(addressBookDTO.getCountyId()) ? areaMap.get(addressBookDTO.getCountyId()).getName() : "");
            addressBookDTO.setFullAddress(addressBookDTO.getProvince() + addressBookDTO.getCity() + addressBookDTO.getCounty() + addressBookDTO.getAddress());
            return addressBookDTO;
        }).collect(Collectors.toList());

        return Result.ok().put("data", PageResponse.<AddressBookDTO>builder()
                .counts(result.getCounts())
                .pages(result.getPages())
                .page(result.getPage())
                .pagesize(result.getPagesize())
                .items(newItems)
                .build());
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "新增")
    public Result save(@RequestBody AddressBook entity) {
        //获取userid
        String userId = RequestContext.getUserId();
        entity.setUserId(userId);
        return addressBookFeign.save(entity);
    }

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    @ApiImplicitParam(name = "id", value = "主键", required = true)
    @PutMapping("/{id}")
    @ApiOperation(value = "修改")
    public Result update(@PathVariable(name = "id") String id, @RequestBody AddressBook entity) {
        //获取userid
        String userId = RequestContext.getUserId();
        entity.setUserId(userId);
        return addressBookFeign.update(id, entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiImplicitParam(name = "id", value = "主键", required = true)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public Result del(@PathVariable(name = "id") String id) {
        return addressBookFeign.del(id);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "明细")
    @ApiImplicitParam(name = "id", value = "主键", required = true)
    @GetMapping("detail/{id}")
    public Result detail(@PathVariable(name = "id") String id) {
        AddressBook addressBook = addressBookFeign.detail(id);
        if (addressBook == null) {
            return Result.error();
        }
        Set<Long> areaSet = new HashSet<>();
        areaSet.add(addressBook.getProvinceId());
        areaSet.add(addressBook.getCityId());
        areaSet.add(addressBook.getCountyId());
        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = areaMapFuture.get();

        AddressBookDTO addressBookDTO = new AddressBookDTO();
        BeanUtils.copyProperties(addressBook, addressBookDTO);
        addressBookDTO.setProvince(areaMap.containsKey(addressBookDTO.getProvinceId()) ? areaMap.get(addressBookDTO.getProvinceId()).getName() : "");
        addressBookDTO.setCity(areaMap.containsKey(addressBookDTO.getCityId()) ? areaMap.get(addressBookDTO.getCityId()).getName() : "");
        addressBookDTO.setCounty(areaMap.containsKey(addressBookDTO.getCountyId()) ? areaMap.get(addressBookDTO.getCountyId()).getName() : "");
        addressBookDTO.setFullAddress(addressBookDTO.getProvince() + addressBookDTO.getCity() + addressBookDTO.getCounty() + addressBookDTO.getAddress());

        return Result.ok().put("data",addressBookDTO);


    }
}
