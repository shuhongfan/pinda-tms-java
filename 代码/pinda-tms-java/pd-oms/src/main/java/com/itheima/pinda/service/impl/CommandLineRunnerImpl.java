package com.itheima.pinda.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * 项目启动时执行当前类中的run方法
 */
@Service
@Slf4j
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Autowired
    private ReloadDroolsRulesService reloadDroolsRulesService;

    @Override
    public void run(String... args) throws Exception {
        reloadDroolsRulesService.reload();
    }
}
