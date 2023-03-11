package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.Member;
import com.itheima.pinda.service.IMemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户前端控制器
 */
@Log4j2
@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("")
    public Result save(@RequestBody Member entity) {
        boolean result = memberService.save(entity);
        if (result) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public Member detail(@PathVariable(name = "id") String id) {
        Member Member = memberService.getById(id);
        return Member;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public PageResponse<Member> page(Integer page, Integer pageSize) {
        Page<Member> iPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        Page<Member> pageResult = memberService.page(iPage, wrapper);

        return PageResponse.<Member>builder()
                .items(pageResult.getRecords())
                .page(page)
                .pagesize(pageSize)
                .pages(pageResult.getPages())
                .counts(pageResult.getTotal())
                .build();
    }

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Member entity) {
        entity.setId(id);
        boolean result = memberService.updateById(entity);
        if (result) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result del(@PathVariable(name = "id") String id) {
        boolean result = memberService.removeById(id);
        if (result) {
            return Result.ok();
        }
        return Result.error();
    }
}