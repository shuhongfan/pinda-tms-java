package com.shf.drools.test;

import com.shf.drools.entity.ComparisonOperatorEntity;
import com.shf.drools.entity.Order;
import com.shf.drools.entity.Student;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.internal.utils.KieService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;

public class DroolsTest {
    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();

//        获得Kie容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();

//        从Kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

//        Fact对象，事实对象
        Order order = new Order();
        order.setOriginalPrice(500d);

        session.insert(order);

//        激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules();

//        关闭会话
        session.dispose();
        System.out.println(order.getRealPrice());
    }

    @Test
    public void test2() {
        KieServices kieServices = KieServices.Factory.get();

//        获得Kie容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();

//        从Kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

//        Fact对象，事实对象
        ComparisonOperatorEntity comparisonOperatorEntity = new ComparisonOperatorEntity();
        comparisonOperatorEntity.setNames("王五");

        ArrayList<String> list = new ArrayList<>();
        list.add("张三2");
        list.add("李四");
        comparisonOperatorEntity.setList(list);

        session.insert(comparisonOperatorEntity);

//        激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules(new RuleNameEndsWithAgendaFilter("rule_comparison_notcontains"));

//        关闭会话
        session.dispose();
    }

    /**
     * update
     */
    @Test
    public void test3() {
        KieServices kieServices = KieServices.Factory.get();

//        获得Kie容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();

//        从Kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

//        Fact对象，事实对象
        Student student = new Student();
        student.setAge(5);

        session.insert(student);

//        激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules();

//        关闭会话
        session.dispose();
    }
}
