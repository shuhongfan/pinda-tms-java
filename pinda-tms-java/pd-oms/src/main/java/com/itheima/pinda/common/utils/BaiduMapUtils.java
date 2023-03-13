package com.itheima.pinda.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 百度地图操作工具类
 */
public class BaiduMapUtils {
    static String AK = "UEBQm9c3KZ5LrsO2C2qsOAs1eSdLvlzM"; // 百度地图密钥

    public static void main(String[] args) {
//        String dom = "北京金燕龙";
//        String coordinate = getCoordinate(dom);
//        System.out.println("'" + dom + "'的经纬度为：" + coordinate);
        String begin = getCoordinate("北京金燕龙");
        String end = getCoordinate("北京龙禧苑一区");
        int time = getTime(begin, end);
        double distance = getDistance(begin, end);

        DecimalFormat df = new DecimalFormat("#.##");
        String distanceStr = df.format(distance / 1000);
        BigDecimal orderDistance = new BigDecimal(distanceStr);
        System.out.println("时间-->" + time/60 + "分钟--距离-->" + orderDistance+"km");
    }

    // 调用百度地图API根据的地址，获取坐标
    public static String getCoordinate(String address) {
        if (address != null && !"".equals(address)) {
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String url = "http://api.map.baidu.com/geocoding/v3/?output=json&ak=" + AK + "&callback=showLocation&address=" + address;
            String json = loadJSON(url);
            json = StringUtils.substringBetween(json, "showLocation(", ")");
            if (json != null && !"".equals(json)) {
                Map map = JSON.parseObject(json, Map.class);
                if ("0".equals(map.getOrDefault("status", "500").toString())) {
                    Map childMap = (Map) map.get("result");
                    Map posMap = (Map) childMap.get("location");
                    double lng = Double.parseDouble(posMap.getOrDefault("lng", "0").toString()); // 经度
                    double lat = Double.parseDouble(posMap.getOrDefault("lat", "0").toString()); // 纬度
                    DecimalFormat df = new DecimalFormat("#.######");
                    String lngStr = df.format(lng);
                    String latStr = df.format(lat);
                    return lngStr + "," + latStr;
                }
            }
        }
        return null;
    }


    public static Integer getTime(String origin, String destination) {
        String[] originArray = origin.split(",");
        String[] destinationArray = destination.split(",");
        origin = originArray[1] + "," + originArray[0];
        destination = destinationArray[1] + "," + destinationArray[0];
        String url = "http://api.map.baidu.com/directionlite/v1/driving?origin=" + origin + "&destination=" + destination + "&ak=" + AK;
        String json = loadJSON(url);
        System.out.println("json-->" + json);
        if (json != null && !"".equals(json)) {
            Map map = JSON.parseObject(json, Map.class);
            if ("0".equals(map.getOrDefault("status", "500").toString())) {
                Map childMap = (Map) map.get("result");
                JSONArray jsonArray = (JSONArray) childMap.get("routes");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                Map posMap = (Map) jsonObject.get("routes");
                int duration = Integer.parseInt(jsonObject.get("duration") == null ? "0" : jsonObject.get("duration").toString());
                return duration;
            }
        }

        return null;
    }

    //根据起止位置经纬度获取距离，单位：米
    public static Double getDistance(String origin, String destination) {
        String[] originArray = origin.split(",");
        String[] destinationArray = destination.split(",");
        origin = originArray[1] + "," + originArray[0];
        destination = destinationArray[1] + "," + destinationArray[0];
        String url = "http://api.map.baidu.com/directionlite/v1/driving?origin=" + origin + "&destination=" + destination + "&ak=" + AK;
        String json = loadJSON(url);
        System.out.println("json-->" + json);
        if (json != null && !"".equals(json)) {
            Map map = JSON.parseObject(json, Map.class);
            if ("0".equals(map.getOrDefault("status", "500").toString())) {
                Map childMap = (Map) map.get("result");
                JSONArray jsonArray = (JSONArray) childMap.get("routes");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                Map posMap = (Map) jsonObject.get("routes");
                double distance = Double.parseDouble(jsonObject.get("distance") == null ? "0" : jsonObject.get("distance").toString());
                return distance;
            }
        }

        return null;
    }

    //访问接口地址，返回结果数据
    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

}