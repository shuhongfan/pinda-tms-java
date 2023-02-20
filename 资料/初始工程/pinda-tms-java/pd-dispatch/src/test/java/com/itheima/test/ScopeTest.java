package com.itheima.test;

import java.awt.geom.Path2D;

public class ScopeTest {
    public static void main(String[] args) {
        Path2D.Double generalPath = new Path2D.Double();

        //绘制区域范围
        generalPath.moveTo(0,0);
        generalPath.lineTo(1,0);
        generalPath.lineTo(1,1);
        generalPath.lineTo(0,1);
        generalPath.lineTo(0,0);
        generalPath.closePath();

        //判断指定点是否在上面的区域范围内
        boolean contains = generalPath.contains(1.5, 1.5);
        System.out.println(contains);
    }
}
