package com.itheima.pinda.service.impl;

import com.itheima.pinda.entity.fact.AddressRule;
import com.itheima.pinda.service.DroolsRulesService;

import java.math.BigDecimal;

public class DroolsRulesServiceImpl implements DroolsRulesService {
    /**
     * 根据条件计算订单价格
     * @param addressRule
     * @return
     */
    public String calcFee(AddressRule addressRule) {
        //货物总重量
        BigDecimal totalWeight = new BigDecimal(addressRule.getTotalWeight());
        //首重
        BigDecimal firstWeight = new BigDecimal(addressRule.getFirstWeight());
        //首重价格
        BigDecimal firstFee = new BigDecimal(addressRule.getFirstFee());
        //续重价格
        BigDecimal continuedFee = new BigDecimal((addressRule.getContinuedFee()));

        //超过首重部分重量=总重量-首重
        BigDecimal lost = totalWeight.subtract(firstWeight);//3.5
        //只保留整数部分
        lost = lost.setScale(0,BigDecimal.ROUND_DOWN);
        return continuedFee.multiply(lost).add(firstFee).toString();
    }

    public static void main(String[] args) {
        AddressRule addressRule = new AddressRule();
        addressRule.setTotalWeight(5.3);
        addressRule.setFirstWeight(1);
        addressRule.setFirstFee(20);
        addressRule.setContinuedFee(6);
        String s = new DroolsRulesServiceImpl().calcFee(addressRule);
        System.out.println(s);
    }
}
