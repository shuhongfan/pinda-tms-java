package com.itheima.pinda.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 接口调用工具类
 */
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);
        cm.setDefaultMaxPerRoute(50);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }


    public static String postForGet(String url) {
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            response = httpClient.execute(httpPost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            logger.error("postForGet IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post访问，设置等待超时时长(单位：ms)
     *
     * @param url
     * @param timeout
     * @return 返回post调用结果，默认为 ""
     * @throws ConnectTimeoutException  连接过程中执行错误时，抛出IO异常。用于超时时抛出异常的检测
     * @throws HttpServerErrorException 状态码大于等于500时，抛出该异常。属于运行时异常
     */
    public static String postForGet(String url, int timeout) throws ConnectTimeoutException {
        String result = "";
        CloseableHttpResponse response;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {//连接超时异常则抛出, 其他异常忽略
                throw (ConnectTimeoutException) e;
            }
            logger.error("postForGet_timeout execute异常, url: {}, error: {}", url, e);
            return result;
        }
        if (response == null)
            return result;
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 500) {
            HttpStatus httpStatus = HttpStatus.resolve(statusCode);
            throw new HttpServerErrorException(httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            result = sb.toString();
        } catch (IOException e) {
            logger.error("postForGet_timeout stream异常, url: {}, error: {}", url, e);
        } finally {
            try {
                response.close();
            } catch (IOException ignore) {
            }
        }
        return result;
    }

    public static String get(String url) {
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            response = httpClient.execute(httpGet);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            result = sb.toString();
            logger.info("返回结果result--->" + result);
        } catch (IOException e) {
            logger.error("get IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * GET请求支持设置超时
     *
     * @param url
     * @param timeout
     * @return
     * @throws ConnectTimeoutException
     */
    public static String get(String url, int timeout) throws ConnectTimeoutException {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        httpGet.setConfig(requestConfig);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("Content-type", "application/json; charset=utf-8");
        httpGet.setHeader("Accept", "application/json");

        String result = "";
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {//连接超时异常则抛出, 其他异常忽略
                throw (ConnectTimeoutException) e;
            }
            logger.error("get_timeout execute异常, url: {}, error: {}", url, e);
            return result;
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            result = sb.toString();
        } catch (IOException e) {
            logger.error("get_timeout IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException ignored) {
            }
        }
        return result;
    }

    public static String post(String url, Map<String, Object> param) {
        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
//            httpPost.setHeader("Accept", "application/json");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> map : param.entrySet()) {
                params.add(new BasicNameValuePair(map.getKey(), map.getValue() == null ? "" : map.getValue().toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            result = IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("post IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String doPost(String url, JSONObject json) {
        long begin = System.currentTimeMillis();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            StringEntity s = new StringEntity("");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.addHeader("Content-Type", "application/json");
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            result = EntityUtils.toString(res.getEntity());// 返回json格式：
            logger.info("返回结果-->" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info(url + "vpn-server耗时：" + (end - begin) / 1000);
        return result;
    }

    public static String doPostWithBody(String url, JSONObject json) {
        logger.info("请求参数--->" + json == null ? "" : json.toString());
        long begin = System.currentTimeMillis();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            StringEntity s = new StringEntity(json == null ? "" : json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.addHeader("Content-Type", "application/json");
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            result = EntityUtils.toString(res.getEntity());// 返回json格式：
            logger.info("返回结果-->" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info(url + "耗时：" + (end - begin) / 1000);
        return result;
    }


    public static String postByMap(String url, Map<String, Object> param) {
        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(param)));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            result = IOUtils.toString(in, HTTP.UTF_8);
            logger.info("postByMap返回结果-->" + result);
        } catch (IOException e) {
            logger.error("postByMap IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String postUpgradeByMap(String url, Map<String, Object> param) {
        CloseableHttpResponse response = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            Integer timeout = Integer.parseInt(param.getOrDefault("timeout", "30000").toString());
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(param)));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            result = IOUtils.toString(in, HTTP.UTF_8);
            logger.info("postUpgradeByMap返回结果-->" + result);
        } catch (IOException e) {
            logger.error("postUpgradeByMap IO异常, url: {}, error: {}", url, e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 拼接get请求url参数
     *
     * @param originUrl 不能为空
     * @param params
     * @return 拼接后的url
     */
    public static String joinGetUrlByParams(String originUrl, Map<String, Object> params) {
        String url;
        String s = buildGetParams(params);
        int paramIdIdx = originUrl.indexOf("?");
        if (paramIdIdx < 0)
            url = originUrl + "?" + s;
        else if (paramIdIdx == originUrl.length() - 1) {//?在最后一位 直接 +
            url = originUrl + s;
        } else {
            url = originUrl + "&" + s;
        }
        while (url.endsWith("?") || url.endsWith("&"))
            url = url.substring(0, url.length() - 1);
        return url;
    }

    /**
     * 根据map对象元素构建get请求拼接参数
     * 形如：a=v1&b=v2
     *
     * @param params
     * @return 空则返回空串
     */
    private static String buildGetParams(Map<String, Object> params) {
        if (MapUtils.isEmpty(params))
            return "";
        StringBuilder urlParams = new StringBuilder();
        String paramSeparator = "&";
        String keyValueSeparator = "=";
        params.entrySet().stream().filter(e -> StringUtils.isNotBlank(e.getKey()))
                .forEach(e -> {
                    try {
                        urlParams.append(paramSeparator).append(e.getKey())
                                .append(keyValueSeparator)
                                .append(e.getValue() == null ? "" :
                                        URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8.name()));
                    } catch (UnsupportedEncodingException ex) {
                        logger.error("构建参数编码错误：params: {}, error: {}", params, ex);
                    }
                });
        return urlParams.length() > 0 ? urlParams.substring(1) : "";
    }

    /**
     * 将url中参数解析成map对象
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException 解析url编码格式时出错
     */
    public static Map<String, Object> parseGetUrlParams(String url) throws UnsupportedEncodingException {
        String[] uriParams = url.split("\\?", 2);
        if (uriParams.length != 2) {
            return Collections.emptyMap();
        }
        String paramStr = uriParams[1];
        String[] kvs = paramStr.split("&");
        Map<String, Object> res = new HashMap<>(kvs.length);
        for (String kv : kvs) {
            if (StringUtils.isNotBlank(kv)) {
                String[] kvStr = kv.split("=", 2);
                if (kvStr.length != 2)
                    continue;
                res.put(kvStr[0], URLDecoder.decode(kvStr[1], "UTF-8"));
            }
        }
        return res;
    }

    /**
     * 去除url后的?及参数字符串，返回纯url
     *
     * @param url
     * @return
     */
    public static String getNoParamsUrl(String url) {
        String[] split = url.split("\\?", 2);
        return split[0];
    }
}