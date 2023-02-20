package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.AddressBook;
import com.itheima.pinda.service.IAddressBookService;
import lombok.extern.log4j.Log4j2;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地址簿
 */
@Log4j2
@RestController
@RequestMapping("cache")
public class J2cacheController {
    /**
     * 查询地址簿详情
     *
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    @Cache(region = "addressBook",key = "ab",params = "id")
    public AddressBook detail(@PathVariable(name = "id") String id) {//100

        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setAddress("beijing");
        System.out.println("查询数据库...");

        return addressBook;
    }

    /**
     * 从j2cache中获取缓存数据
     *
     * @return
     */
    @GetMapping("/getCacheData/{id}")
    @Cache(region = "rx", key = "user",params = "id")
    public List<String> getCacheData(@PathVariable(name = "id") String id) {//key = user:123
        //没有获取到缓存数据，需要从数据库中查询数据
        List<String> data = new ArrayList<>();
        data.add("beijing");
        data.add("nanjing");
        data.add("shanghai");
        System.out.println("查询数据库");

        return data;
    }

    /**
     * 从j2cache中获取缓存数据
     *
     * @return
     */
    @GetMapping("/getCacheDataBody")
    @Cache(region = "rx", key = "user",params = "0.name")
    public List<Map> getCacheDataBody(@RequestBody Map body) {//key = user:xiaoming
        //没有获取到缓存数据，需要从数据库中查询数据
        List<Map> data = new ArrayList<>();
        data.add(body);
        System.out.println("查询数据库");
        return data;
    }

    /**
     * 从j2cache中获取缓存数据
     *
     * @return
     */
    @GetMapping("/getCacheDataParams")
    @Cache(region = "rx", key = "user",params = "0.name")
    public List<Map> getCacheDataParams(@RequestParam Map params) {
        //没有获取到缓存数据，需要从数据库中查询数据
        List<Map> data = new ArrayList<>();
        data.add(params);
        System.out.println("查询数据库");
        return data;
    }

    @GetMapping("/getAllData")
    @Cache(region = "rx", key = "users")
    public List<String> getAllData() {//key = users
        //没有获取到缓存数据，需要从数据库中查询数据
        List<String> data = new ArrayList<>();
        data.add("beijing");
        data.add("nanjing");
        data.add("shanghai");
        System.out.println("查询数据库");
        return data;
    }

    /**
     * 清理指定缓存数据
     *
     * @return
     */
    @CacheEvictor(value = {@Cache(region = "rx",key = "user", params = "id")})
    @GetMapping("/evict/{id}")
    public String evict(@PathVariable(name = "id") String id) {//key = user:123
        System.out.println("删除数据库");
        return "evict success";
    }

    /**
     * 清理指定缓存数据
     *
     * @return
     */
    @CacheEvictor({@Cache(region = "rx",key = "users")})//key = users
    @GetMapping("/evict")
    public String evictAll() {
        System.out.println("删除数据库");
        return "evict success";
    }
}