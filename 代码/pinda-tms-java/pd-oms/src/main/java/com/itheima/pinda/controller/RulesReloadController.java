package com.itheima.pinda.controller;

import com.itheima.pinda.service.impl.ReloadDroolsRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@RequestMapping("/rules")
@Controller
public class RulesReloadController {
    @Autowired
    private ReloadDroolsRulesService reloadDroolsRulesService;

    /**
     * 从数据库加载最新规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        reloadDroolsRulesService.reload();
        return "ok";
    }
}
