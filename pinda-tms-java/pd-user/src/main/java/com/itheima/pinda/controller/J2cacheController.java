package com.itheima.pinda.controller;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试j2cache缓存相关操作
 */
@RestController
@RequestMapping("/cache")
public class J2cacheController {
    /**
     * 从j2cache中获取缓存数据
     *
     * @return
     */
    @GetMapping("/getCacheData/{id}")
    @Cache(region = "rx", key = "user",params = "id")
    public List<String> getCacheData(@PathVariable(name = "id") String id) {
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
    public List<Map> getCacheDataBody(@RequestBody Map body) {
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
    public List<String> getAllData() {
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
    @CacheEvictor({@Cache(region = "rx",key = "user", params = "id")})
    @GetMapping("/evict/{id}")
    public String evict(@PathVariable(name = "id") String id) {
        System.out.println("删除数据库");
        return "evict success";
    }

    /**
     * 清理指定缓存数据
     *
     * @return
     */
    @CacheEvictor({@Cache(region = "rx",key = "users")})
    @GetMapping("/evict")
    public String evictAll() {
        System.out.println("删除数据库");
        return "evict success";
    }
}