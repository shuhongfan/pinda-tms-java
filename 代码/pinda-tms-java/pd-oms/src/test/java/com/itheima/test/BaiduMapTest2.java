package com.itheima.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 测试百度地图提供的地理编码HTTP服务接口
 */
public class BaiduMapTest2 {
    public static void main(String[] args) throws Exception{
        String ak = "UEBQm9c3KZ5LrsO2C2qsOAs1eSdLvlzM";
        String origin = "40.01116,116.339303"; //起点经纬度，格式为：纬度,经度；小数点后不超过6位
        String destination = "39.936404,116.452562"; //终点经纬度，格式为：纬度,经度；小数点后不超过6位
        String httpUrl = "http://api.map.baidu.com/directionlite/v1/driving?origin="
                +origin+"&destination="
                +destination+"&ak=" + ak;


        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        System.out.println(json.toString());


        String data = json.toString();
        if (data != null && !"".equals(data)) {
            Map map = JSON.parseObject(data, Map.class);
            if ("0".equals(map.getOrDefault("status", "500").toString())) {
                Map childMap = (Map) map.get("result");
                JSONArray jsonArray = (JSONArray) childMap.get("routes");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                double distance = Double.parseDouble(jsonObject.get("distance") == null ? "0" : jsonObject.get("distance").toString());
                System.out.println(distance);
            }
        }

    }
}
