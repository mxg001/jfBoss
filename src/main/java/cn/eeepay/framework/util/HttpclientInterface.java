package cn.eeepay.framework.util;

import cn.eeepay.framework.exception.InterfaceException;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Administrator on 2019/7/17/017.
 * @author  liuks
 *  Httpclient 的请求
 */
public class HttpclientInterface {


    private static final Logger log = LoggerFactory.getLogger(HttpclientInterface.class);

    /**
     * 调用积分后台的验签加密规则
     * @param signKey
     * @param signMap
     * @return
     */
    public static String sign(String signKey ,Map<String,Object> signMap){
        StringBuffer sb = new StringBuffer();
        for (String key : signMap.keySet() ){
            sb.append(key).append("=").append(signMap.get(key)).append("&");
        }
        String signStr = sb.toString();
        signStr = signStr.substring(0,signStr.length() -1);
        signStr = SecureUtil.md5(signStr + signKey);
        return signStr;
    }

    /**
     * 公共httpPost请求方法
     */
    public static String httpPost(String url, Map<String, Object> map){
        log.info("请求路径:{},参数:{}",url, JSONObject.toJSONString(map));
        String jsonStr = "";
        CloseableHttpClient httpclient = null;
        HttpPost httppost = null;
        try {
            httpclient = HttpClientBuilder.create().build();
            httppost = new HttpPost(url);
            //配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000).build();
            httppost.setConfig(requestConfig);

            httppost.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

            if (null != map) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if(entry!=null){
                        params.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue()==null?"":entry.getValue().toString()));
                    }
                }
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }

            CloseableHttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity rs = response.getEntity();
                jsonStr = EntityUtils.toString(rs);
            }
            log.info("返回结果：{}", jsonStr);
            httppost.releaseConnection();
            httpclient.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (null != httppost) {
                    httppost.releaseConnection();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                if (null != httpclient) {
                    httpclient.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return jsonStr;
    }
}
