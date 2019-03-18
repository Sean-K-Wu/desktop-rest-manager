package com.wuxiangknow.rest.util;

import com.alibaba.fastjson.JSONObject;
import com.wuxiangknow.rest.thread.ThreadPoolManager;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Descirption http工具类
 * @Author WuXiang
 * @Date 2018/6/26/
 */
public class HttpClientUtil {





    public static byte[] doget(String url,Component parentComponent) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            HttpEntity entity2 = response.getEntity();
            return EntityUtils.toByteArray(entity2);
        } catch (IOException e) {
            e.printStackTrace();
            handleFailedRequest(parentComponent);
        }
        return null;
    }


    /**
     * 处理post请求
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONObject doPost(String url , Map<String,String> map,Component parentComponent) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(map.size());
        Set<String> strings = map.keySet();
        CloseableHttpResponse response2 = null;
        int status = 200;
        for (String string : strings) {
            nvps.add(new BasicNameValuePair(string, map.get(string)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            response2 = httpclient.execute(httpPost);
            status = response2.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            HttpEntity entity2 = response2.getEntity();
            String s = EntityUtils.toString(entity2);
            JSONObject jsonObject = JSONObject.parseObject(s);
            return jsonObject;
        }
        catch (Exception e){
            if((e instanceof ClientProtocolException &&  (status < 200 || status >= 300))
                    || e instanceof HttpHostConnectException
                    ){
                handleFailedRequest(parentComponent);
            }
           e.printStackTrace();
        }
        finally {
            if(response2!=null){
                try {
                    response2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void handleFailedRequest(final Component parentComponent) {
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showConfirmDialog(parentComponent, "网络异常", "提示", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
    }


}
