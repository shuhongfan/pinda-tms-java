package com.itheima.pinda.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 内存分页工具列
 * @author jpfss
 */
public class RamPageUtils {

    /**
     * 默认每页条数
     */
    public static final Integer PAGE_SIZE = 10;

    /**
     * 分页方法
     *
     * @param dataList
     * @param pageNum
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> List<T> page(List<T> dataList, int pageNum, int pageSize) {

        if (null == dataList || dataList.size() == 0) {
            return dataList;
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }

        int from = (pageNum - 1) * pageSize;
        int to = pageNum * pageSize;
        if (to > dataList.size()) {
            to = dataList.size();
        }

        if (from >= dataList.size() || to <= from) {
            return Collections.emptyList();
        }
        return dataList.subList(from,to);
    }


    /**
     * 默认每页条数进行分页
     * @param dataList
     * @param pageNum
     * @param <T>
     * @return
     */
    public static <T> List<T> page(List<T> dataList,int pageNum){
        return page(dataList,pageNum,PAGE_SIZE);
    }

}
