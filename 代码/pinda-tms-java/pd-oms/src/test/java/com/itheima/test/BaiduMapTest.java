package com.itheima.test;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 测试百度地图提供的地理编码HTTP服务接口
 */
public class BaiduMapTest {
    public static void main(String[] args) throws Exception{
        String ak = "UEBQm9c3KZ5LrsO2C2qsOAs1eSdLvlzM";//注意，ak类型必须为服务端类型
        String address = "北京市育新花园小区";
        String httpUrl = "http://api.map.baidu.com/geocoding/v3/?address=" + address + "&output=json&ak=" + ak;

        URL url = new URL(httpUrl);
        URLConnection urlConnection = url.openConnection();

        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = urlConnection.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String inputLine = null;
        while ((inputLine = bufferedReader.readLine()) != null){
            stringBuilder.append(inputLine);
        }

        bufferedReader.close();

        System.out.println(stringBuilder.toString());

        Map map = JSON.parseObject(stringBuilder.toString(), Map.class);
        System.out.println(map);

        String status = map.get("status").toString();
        if(status.equals("0")){
            //返回结果成功，能够正常解析地址信息
            Map result = (Map) map.get("result");
            Map location = (Map) result.get("location");
            String lng = location.get("lng").toString();
            String lat = location.get("lat").toString();

            DecimalFormat df = new DecimalFormat("#.######");
            String lngStr = df.format(Double.parseDouble(lng));
            String latStr = df.format(Double.parseDouble(lat));
            String r = lngStr + "," + latStr;
            System.out.println(r);
        }

    }
}
