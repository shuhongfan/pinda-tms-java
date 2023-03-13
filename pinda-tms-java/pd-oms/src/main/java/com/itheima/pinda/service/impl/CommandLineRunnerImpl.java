package com.itheima.pinda.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动时加载最新规则
 **/
@Component
@Slf4j
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Resource
    private ReloadDroolsRulesService rules;

    @Override
    public void run(String... args) throws Exception {
        rules.reload();
    }
}