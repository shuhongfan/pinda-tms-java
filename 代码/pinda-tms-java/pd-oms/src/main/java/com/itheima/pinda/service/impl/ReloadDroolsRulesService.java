package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.pinda.entity.Rule;
import com.itheima.pinda.mapper.RuleMapper;
import org.apache.poi.ss.formula.functions.T;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 重新加载数据库中的规则，创建Drools相关对象
 */
@Service
public class ReloadDroolsRulesService {
    @Autowired
    private RuleMapper ruleMapper;

    //全局唯一容器对象，用于创建Session操作Drools
    public static KieContainer kieContainer;

    /**
     * 查询数据库中所有的规则
     * @return
     */
    public List<Rule> loadRules(){
        QueryWrapper<Rule> wrapper = new QueryWrapper<>();
        wrapper.eq("rule_key","tms");
        return ruleMapper.selectList(wrapper);
    }

    /**
     * 重新创建KieContainer对象
     */
    public void reload(){
        KieContainer kieContainer = this.loadContainerFromString(loadRules());
        this.kieContainer = kieContainer;
    }

    /**
     * 根据规则内容创建KieContainer对象
     * @param ruleList
     * @return
     */
    public KieContainer loadContainerFromString(List<Rule> ruleList){
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        KieFileSystem kfs = ks.newKieFileSystem();//文件系统

        for (Rule rule : ruleList) {
            String drl = rule.getContent();
            kfs.write("src/main/resources/" + drl.hashCode() + ".drl",drl);
        }

        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();

        KieContainer kieContainer = ks.newKieContainer(kr.getDefaultReleaseId());

        return kieContainer;
    }
}
