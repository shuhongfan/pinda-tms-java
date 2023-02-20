package com.itheima.pinda.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EntCoordSyncJob
 * @Description: 根据地理位置获取坐标
 */
public class EntCoordSyncJob {
    static String AK = "MVnGC6qGSlY1i0iTv3Gbo9zsG6O94bex"; // 百度地图密钥

    public static void main(String[] args) {
//        String dom = "北京金燕龙";
//        String coordinate = getCoordinate(dom);
//        System.out.println("'" + dom + "'的经纬度为：" + coordinate);
        // System.err.println("######同步坐标已达到日配额6000限制，请明天再试！#####");
        String begin = EntCoordSyncJob.getCoordinate("北京建材城金燕龙办公楼");
        String end = EntCoordSyncJob.getCoordinate("北京龙禧苑一区");
//        int time = getTime(begin, end);
//        double distance = getDistance(begin, end);
//        System.out.println("时间-->" + time / 60 + "分钟--距离-->" + distance / 1000 + "km");
        getLocationByPosition(begin);
    }

    // 调用百度地图API根据地址，获取坐标
    public static String getCoordinate(String address) {
        if (address != null && !"".equals(address)) {
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String url = "http://api.map.baidu.com/geocoding/v3/?output=json&ak=" + AK + "&callback=showLocation&address=" + address;
//            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + AK;
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

    /**
     *
     * @param location
     * @return map
     * formatted_address:结构化地址信息
     *       "country":"国家",
     *             "country_code":国家编码,
     *             "country_code_iso":"国家英文缩写（三位）",
     *             "country_code_iso2":"国家英文缩写（两位）",
     *             "province":"省名",
     *             "city":"城市名",
     *             "city_level":城市所在级别（仅国外有参考意义。国外行政区划与中国有差异，城市对应的层级不一定为『city』。country、province、city、district、town分别对应0-4级，若city_level=3，则district层级为该国家的city层级）,
     *             "district":"区县名",
     *             "town":"乡镇名",
     *             "town_code":"乡镇id",
     *             "adcode":"行政区划代码",
     *             "street":"街道名（行政区划中的街道层级）",
     *             "street_number":"街道门牌号",
     *             "direction":"和当前坐标点的方向",
     *             "distance":"离坐标点距离"
     */
    public static Map getLocationByPosition(String location) {
        String[] originArray = location.split(",");
        location = originArray[1] + "," + originArray[0];
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + AK + "&output=json&coordtype=wgs84ll&location=" + location;
//        String url = "http://api.map.baidu.com/directionlite/v1/driving?origin=" + origin + "&destination=" + destination + "&ak=" + AK;
        String json = loadJSON(url);
        System.out.println("json-->" + json);
        if (json != null && !"".equals(json)) {
            Map map = JSON.parseObject(json, Map.class);
            if ("0".equals(map.getOrDefault("status", "500").toString())) {
                Map childMap = (Map) map.get("result");
                Map areaMap = (Map) childMap.get("addressComponent");
                areaMap.put("formatted_address", childMap.getOrDefault("formatted_address", "").toString());
                return areaMap;
            }
        }

        return null;
    }

    /**
     * 判断点是否在区域内
     *
     * @param polygon   区域经纬度集合
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    public static boolean isInScope(List<Map> polygon, double longitude, double latitude) {
        Path2D.Double generalPath = new Path2D.Double();

        //获取第一个起点经纬度的坐标
        Map first = polygon.get(0);

        //通过移动到以double精度指定的指定坐标，把第一个起点添加到路径中
        generalPath.moveTo(Double.parseDouble(first.getOrDefault("lng", "").toString()), Double.parseDouble(first.getOrDefault("lat", "").toString()));

        //把集合中的第一个点删除防止重复添加
        polygon.remove(0);

        //循环集合里剩下的所有经纬度坐标
        for (Map d : polygon) {
            //通过从当前坐标绘制直线到以double精度指定的新指定坐标，将路径添加到路径。
            //从第一个点开始，不断往后绘制经纬度点
            generalPath.lineTo(Double.parseDouble(d.getOrDefault("lng", "").toString()), Double.parseDouble(d.getOrDefault("lat", "").toString()));

        }

        // 最后要多边形进行封闭，起点及终点
        generalPath.lineTo(Double.parseDouble(first.getOrDefault("lng", "").toString()), Double.parseDouble(first.getOrDefault("lat", "").toString()));

        //将直线绘制回最后一个 moveTo的坐标来关闭当前子路径。
        generalPath.closePath();

        //true如果指定的坐标在Shape边界内; 否则为false 。
        return generalPath.contains(longitude, latitude);
    }
}