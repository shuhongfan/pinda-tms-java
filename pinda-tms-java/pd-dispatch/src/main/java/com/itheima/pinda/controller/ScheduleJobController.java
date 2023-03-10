package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.ImmutableList;
import com.itheima.pinda.DTO.OrgJobTreeDTO;
import com.itheima.pinda.DTO.ScheduleJobDTO;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.ScheduleJobEntity;
import com.itheima.pinda.service.IScheduleJobService;
import com.itheima.pinda.utils.DateUtils;
import com.itheima.pinda.utils.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author
 */
@RestController
@RequestMapping("/schedule")
@Api(tags = "定时任务")
public class ScheduleJobController {
    private static final List<Integer> ORG_TYPE = ImmutableList.of(OrgType.BUSINESS_HALL.getType(), OrgType.TOP_TRANSFER_CENTER.getType(), OrgType.TOP_TRANSFER_CENTER.getType()).asList();

    @Autowired
    private IScheduleJobService scheduleJobService;

    @Autowired
    private OrgApi orgApi;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name", paramType = "query", dataType = "String")
    })
    public R<List<OrgJobTreeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        List<OrgJobTreeDTO> tree = scheduleJobService.page(params);

        return R.success(tree);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ScheduleJobDTO info(@PathVariable("id") String id) {
        ScheduleJobDTO schedule = scheduleJobService.get(id);
        return schedule;
    }

    @GetMapping("dispatch/{id}")
    @ApiOperation("调度信息")
    public Result dispatchInfo(@PathVariable("id") String id) {

        LambdaQueryWrapper<ScheduleJobEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleJobEntity::getBusinessId, id);
        ScheduleJobEntity scheduleJobEntity = scheduleJobService.getOne(wrapper);
        if (scheduleJobEntity == null) {
            return Result.error(404, "机构没有任务信息");
        }

        ScheduleJobDTO schedule = new ScheduleJobDTO();
        BeanUtils.copyProperties(scheduleJobEntity, schedule);
        return Result.ok().put("data", schedule);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody ScheduleJobDTO dto) {

        scheduleJobService.save(dto);

        return Result.ok();
    }

    @PostMapping("dispatch")
    @ApiOperation("保存或修改")
    public Result dispatch(@RequestBody ScheduleJobDTO dto) {

        String businessId = dto.getBusinessId();

        R<Org> orgR = orgApi.get(Long.valueOf(businessId));

        Integer orgType = orgR.getData().getOrgType();
        if (!ORG_TYPE.contains(orgType)) {
            return Result.error(400, "无法给转运中心以上的机构增加调度任务");
        }

        if (StringUtils.isNotBlank(dto.getId())) {
            dto.setUpdateDate(new Date());
            scheduleJobService.update(dto);
            return Result.ok();
        } else {
            dto.setId(IdUtils.get());
            dto.setBeanName("dispatchTask");
            dto.setCreateDate(new Date());
            scheduleJobService.save(dto);

            return Result.ok();
        }
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody ScheduleJobDTO dto) {

        scheduleJobService.update(dto);

        return Result.ok();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody String[] ids) {
        scheduleJobService.deleteBatch(ids);

        return Result.ok();
    }

    @PutMapping("/run/{id}")
    @ApiOperation("立即执行")
    public Result run(@PathVariable String id) {
        scheduleJobService.run(new String[]{id});

        return Result.ok();
    }

    @PutMapping("/run")
    @ApiOperation("立即执行")
    public Result run(@RequestBody String[] ids) {
        scheduleJobService.run(ids);

        return Result.ok();
    }

    @PutMapping("/pause/{id}")
    @ApiOperation("暂停")
    public Result pause(@PathVariable String id) {
        scheduleJobService.pause(new String[]{id});

        return Result.ok();
    }

    @PutMapping("/pause")
    @ApiOperation("暂停")
    public Result pause(@RequestBody String[] ids) {
        scheduleJobService.pause(ids);

        return Result.ok();
    }

    @PutMapping("/resume/{id}")
    @ApiOperation("恢复")
    public Result resume(@PathVariable String id) {
        scheduleJobService.resume(new String[]{id});

        return Result.ok();
    }

    @PutMapping("/resume")
    @ApiOperation("恢复")
    public Result resume(@RequestBody String[] ids) {
        scheduleJobService.resume(ids);

        return Result.ok();
    }

}
