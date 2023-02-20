package com.itheima.pinda.entity.fact;

/**
 * 封装计算订单价格所需的参数
 */
public class AddressRule {
    /**
     * 货品总重量
     */
    private double totalWeight;
    /**
     * 距离
     */
    private double distance;
    /**
     * 续重价格
     */
    private double continuedFee;

    /**
     * 首重
     */
    private double firstWeight;

    /**
     * 首重价格
     */
    private double firstFee;

    public double getFirstFee() {
        return firstFee;
    }

    public void setFirstFee(double firstFee) {
        this.firstFee = firstFee;
    }

    public double getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(double firstWeight) {
        this.firstWeight = firstWeight;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getContinuedFee() {
        return continuedFee;
    }

    public void setContinuedFee(double continuedFee) {
        this.continuedFee = continuedFee;
    }
}
