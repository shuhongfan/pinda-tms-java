package com.itheima.pinda.feign;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.AddressBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pd-user")
@RequestMapping("addressBook")
public interface AddressBookFeign {

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("page")
    PageResponse<AddressBook> page(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize, @RequestParam("userId")String userId,@RequestParam("keyword") String keyword);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("")
    Result save(@RequestBody AddressBook entity);

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    @PutMapping("/{id}")
    Result update(@PathVariable(name = "id") String id, @RequestBody AddressBook entity);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Result del(@PathVariable(name = "id") String id);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    AddressBook detail(@PathVariable(name = "id") String id);
}
