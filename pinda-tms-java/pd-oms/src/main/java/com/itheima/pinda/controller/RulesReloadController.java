package com.itheima.pinda.controller;

import com.itheima.pinda.service.impl.ReloadDroolsRulesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

@RequestMapping("/rules")
@Controller
public class RulesReloadController {
    @Resource
    private ReloadDroolsRulesService rules;

    /**
     * 从数据库加载最新规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "ok";
    }
}