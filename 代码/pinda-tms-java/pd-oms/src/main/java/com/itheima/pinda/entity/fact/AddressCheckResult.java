package com.itheima.pinda.entity.fact;

/**
 * 封装订单价格计算后的结果
 */
public class AddressCheckResult {
    private boolean postCodeResult = false; // true:通过校验；false：未通过校验
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isPostCodeResult() {
        return postCodeResult;
    }

    public void setPostCodeResult(boolean postCodeResult) {
        this.postCodeResult = postCodeResult;
    }
}
