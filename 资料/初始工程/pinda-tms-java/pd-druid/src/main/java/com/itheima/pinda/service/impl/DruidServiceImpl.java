package com.itheima.pinda.service.impl;

import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.mapper.MessageMapper;
import com.itheima.pinda.service.DruidService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("druidService")
public class DruidServiceImpl implements DruidService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 查询全部车辆的当前位置信息
     *
     * @param params
     * @return
     */
    @Override
    public Result queryAllTruckLast(Map<String, Object> params) {
        params.put("type", "truck");
        StringBuffer sql = new StringBuffer("select CONCAT(businessId,'#','" + params.get("type") + "','#',MAX(currentTime)) as id from tms_order_location where 1 = 1");
        sql.append(whereSql(params));
        sql.append("GROUP BY businessId");
        // 查询全部车辆
        List<Map> idMaps = messageMapper.list(sql.toString());

        StringBuffer querySql = new StringBuffer("select currentTime,name,phone,licensePlate,businessId,lat,lng from tms_order_location where id in (");
        for (Map idMap : idMaps) {
            String id = (String) idMap.get("id");
            querySql.append("'").append(id).append("'").append(",");
        }
        querySql.replace(querySql.length() - 1, querySql.length(), ")");
        // 查询最后一次位置信息
        List<Map> messageEntities = messageMapper.list(querySql.toString());

        return Result.ok().put("data", messageEntities);
    }

    /**
     * 查询一个车辆的全部位置信息
     *
     * @param params
     * @return
     */
    @Override
    public Result queryOneTruck(Map<String, Object> params) {
        params.put("type", "truck");
        params.put("order", "__time ASC");
        params.put("limit", 99);
        StringBuffer baseSql = new StringBuffer("select name,phone,licensePlate,businessId,__time from tms_order_location where 1 = 1");
        baseSql.append(whereSql(params));
        baseSql.append(orderSql(params));
        Map baseMap = messageMapper.listFirst(baseSql.toString());
        if (CollectionUtils.isEmpty(baseMap)) {
            return Result.ok();
        }
        // 查询全部车辆
        StringBuffer latLngSql = new StringBuffer("select lat,lng,__time from tms_order_location where 1 = 1");
        latLngSql.append(whereSql(params));
        latLngSql.append(orderSql(params));
        List<Map> messageEntities = messageMapper.list(latLngSql.toString());

        baseMap.put("polyLinePath", messageEntities);
        return Result.ok().put("data", baseMap);
    }

    /**
     * 根据一组参数查询全部经纬度信息
     * @param params
     * @return
     */
    @Override
    public Result queryAll(List<Map<String, Object>> params) {
        List<Map> baseMapList = new ArrayList<>();
        List linePointList = new ArrayList();
        for (Map<String, Object> param : params) {
            param.put("order", "__time ASC");
            StringBuffer baseSql = new StringBuffer("select name,phone,licensePlate,businessId,__time from tms_order_location where 1 = 1");
            baseSql.append(whereSql(param));
            baseSql.append(orderSql(param));
            Map baseMap = messageMapper.listFirst(baseSql.toString());
            if (CollectionUtils.isEmpty(baseMap)) {
                continue;
            }
            // 查询全部车辆
            StringBuffer latLngSql = new StringBuffer("select lat,lng,__time from tms_order_location where 1 = 1");
            latLngSql.append(whereSql(param));
            latLngSql.append(orderSql(param));
            latLngSql.append(limitSql(param));
            List<Map> messageEntities = messageMapper.list(latLngSql.toString());
            linePointList.addAll(messageEntities);
            baseMap.put("list", messageEntities);
            baseMapList.add(baseMap);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("polyLinePath", linePointList);
        resultMap.put("pointsArr", new ArrayList<>());
        return Result.ok().put("data", resultMap);
    }

    /**
     * 查询条件
     *
     * @param params
     * @return
     */
    private String querySQL(Map<String, Object> params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * from tms_order_location where 1 = 1 ");

        String orderSql = orderSql(params);
        String limitSql = limitSql(params);

        String whereSql = whereSql(params);

        stringBuffer.append(whereSql);

        if (orderSql != null) {
            stringBuffer.append(orderSql);
        }

        if (limitSql != null) {
            stringBuffer.append(limitSql);
        }

        log.info("SQL 构建完成:{}  {}", params, stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * 条件 sql
     *
     * @param params
     * @return
     */
    private String whereSql(Map<String, Object> params) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : params.keySet()) {
            if (key.equals("order") || key.equals("limit")) {
                continue;
            }
            String value = (String) params.get(key);
            if (StringUtils.isNotBlank(value)) {
                if (key.contains("_")) {
                    String[] keyArray = key.split("_");
                    String type = keyArray[0];
                    String nkey = key.replace(type + "_", "");

                    stringBuffer.append(" and ").append(nkey);
                    if (type.equals("like")) { // like 特殊处理
                        stringBuffer.append(" like ");
                        stringBuffer.append(" '%").append(value).append("%' ");
                        continue;
                    }
                    if (type.equals("gt")) {  // 大于
                        stringBuffer.append(" > ");
                    }
                    if (type.equals("ge")) {  // 大于等于
                        stringBuffer.append(" >= ");
                    }
                    if (type.equals("lt")) {  // 小于
                        stringBuffer.append(" < ");
                    }
                    if (type.equals("le")) {  // 小于等于
                        stringBuffer.append(" <= ");
                    }
                    if (type.equals("ne")) {  // 不等于
                        stringBuffer.append(" <> ");
                    }
                    stringBuffer.append(" '").append(value).append("' ");
                } else {
                    stringBuffer.append(" and ").append(key);
                    stringBuffer.append(" = ");
                    stringBuffer.append(" '").append(value).append("' ");
                }
            }
        }
        log.info("SQL WHERE 构建完成:{}  {}", params, stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * 排序 sql
     *
     * @param params
     * @return
     */
    private String orderSql(Map<String, Object> params) {
        StringBuffer orderBuffer = null;
        if (params.containsKey("order")) {
            // 排序字段处理后 continue
            // 排序字段 value 为 字段名 +  asc
            orderBuffer = new StringBuffer();
            orderBuffer.append(" order by ").append(params.get("order"));
            log.info("SQL ORDER 构建完成:{}  {}", params, orderBuffer);
            return orderBuffer.toString();
        }
        return null;
    }

    /**
     * 分页 sql
     *
     * @param params
     * @return
     */
    private String limitSql(Map<String, Object> params) {
        StringBuffer limitBuffer = null;
        if (params.containsKey("limit")) {
            limitBuffer = new StringBuffer();
            limitBuffer.append(" limit ").append(params.get("limit"));
            params.remove("limit");
            log.info("SQL LIMIT 构建完成:{}  {}", params, limitBuffer);
            return limitBuffer.toString();
        }
        return null;
    }

}
