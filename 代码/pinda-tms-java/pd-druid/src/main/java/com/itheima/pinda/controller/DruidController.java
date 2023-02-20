package com.itheima.pinda.controller;

import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.service.DruidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "druid平台")
@RequestMapping("apache-druid/query")
@Slf4j
public class DruidController {
    @Autowired
    private DruidService druidService;

    /**
     * 查询Druid中数据列表
     *
     * @param params
     * @return
     */
    @ApiOperation("查询Druid全部车辆的最后位置列表")
    @RequestMapping(value = "/select")
    public Result select(@RequestParam Map<String, Object> params) {
        return druidService.queryAllTruckLast(params);
    }

    @ApiOperation("查询Druid当前车辆的全部位置")
    @RequestMapping(value = "/selectOne")
    public Result selectOne(@RequestParam Map<String, Object> params) {
        return druidService.queryOneTruck(params);
    }

    @ApiOperation("查询Druid当前参数全部位置")
    @RequestMapping(value = "/selectByList")
    public Result selectByList(@RequestBody List<Map<String, Object>> params) {
        return druidService.queryAll(params);
    }
}
