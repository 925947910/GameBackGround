package com.yxb.cms.architect.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;


public class HttpClientUtil {
	private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
//    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getName());
    private final int DEFAULT_TIMEOUT = 30000;

    private static HttpClientUtil ins;

    private HttpClient client;
    private int timeout = DEFAULT_TIMEOUT;

    private static int maxConnTotal = 200;   //最大不要超过1000
    private static int maxConnPerRoute = 100;//实际的单个连接池大小，

    private HttpClientUtil() {
        if (client == null) {
            client = HttpClients.createDefault();
        }
    }

    public static HttpClientUtil getInstance() {
        if (ins == null) {
            synchronized (HttpClientUtil.class) {
                if (ins == null) {
                    ins = new HttpClientUtil();
                }
            }
        }
        return ins;
    }

    public String doGetWithJsonResult(String uri) {
        String json = null;
        log.debug("========= Call [{}] Start ==========", uri);
        HttpResponse response = null;
        try {
            HttpGet request = new HttpGet(uri);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            request.setConfig(config);
            response = client.execute(request);
            log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.debug("Payload : {}", json);
            }
        } catch (Exception e) {
            log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            }
        }
        log.debug("========= Call [{}] End ==========", uri);
        return json;
    }

    public String doGetWithJsonResult(String uri, int timeout) {
        setTimeout(timeout);
        return doGetWithJsonResult(uri);
    }

    public String doGetCustom(String uri) {
        String json = null;
        log.debug("========= Call [{}] Start ==========", uri);
        HttpResponse response = null;
        try {
            HttpGet request = new HttpGet(uri);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();

            request.setConfig(config);
            client = HttpClients.custom().setDefaultRequestConfig(config)
                    .setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxConnPerRoute).build();
            response = client.execute(request);
            log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.debug("Payload : {}", json);
            }
        } catch (Exception e) {
            log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            }
        }
        log.debug("========= Call [{}] End ==========", uri);
        return json;
    }

	/*public <T> T doGetWithJsonResult(String uri, Class<T> javaType) {
		T result = null;
		log.debug("========= Call [{}] Start ==========", uri);
		try {
			HttpGet request = new HttpGet(uri);
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout).setSocketTimeout(timeout).build();
			request.setConfig(config);
			HttpResponse response = client.execute(request);
			log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				log.debug("Payload : {}", json);
				result = JsonUtils.toBean(json, javaType);
			}
		} catch (Exception e) {
			log.error("HttpClient has exception! message: {}", e.getMessage(), e);
			return null;
		}
		log.debug("========= Call [{}] End ==========", uri);
		return result;
	}*/

    public String doPostWithJsonResult(String uri, Map<String, String> paramMap) {
        String json = null;
        log.debug("========= Call [{}] Start ==========", uri);
        HttpResponse response = null;
        try {
            HttpPost request = new HttpPost(uri);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            request.setConfig(config);
            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            if (paramMap != null && !paramMap.isEmpty()) {
                for (String key : paramMap.keySet()) {
                    params.add(new BasicNameValuePair(key, paramMap.get(key)));
                }
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            response = client.execute(request);
            log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.debug("Payload : {}", json);
            }
            request.releaseConnection();
        } catch (Exception e) {
            log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                log.error("HttpClient has exception! message: {}", e.getMessage(), e);
            }
        }
        log.debug("========= Call [{}] End ==========", uri);
        return json;
    }

	/*public <T> T doPostWithJsonResult(String uri, Map<String, String> paramMap, Class<T> javaType) {
		T result = null;
		log.debug("========= Call [{}] Start ==========", uri);
		try {
			HttpPost request = new HttpPost(uri);
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout).setSocketTimeout(timeout).build();
			request.setConfig(config);
			List<NameValuePair> params = new ArrayList<NameValuePair>(0);
			if (paramMap != null && !paramMap.isEmpty()) {
				for (String key : paramMap.keySet()) {
					params.add(new BasicNameValuePair(key, paramMap.get(key)));
				}
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			HttpResponse response = client.execute(request);
			log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				log.debug("Payload : {}", json);
				result = JsonUtils.toBean(json, javaType);
			}
		} catch (Exception e) {
			log.error("HttpClient has exception! message: {}", e.getMessage(), e);
			return null;
		}
		log.debug("========= Call [{}] End ==========", uri);
		return result;
	}*/

	/*public String doPostWithJsonResult(String uri, String jsonParameters) {
		log.debug("======================"+jsonParameters);
		log.debug("========= Call [{}] Start ==========", uri);
		log.debug("========= Call [{}] Start ==========", jsonParameters);
		HttpPost request = new HttpPost(uri);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout).setSocketTimeout(timeout).build();
		request.setConfig(config);
		StringBody stringbody = new StringBody(jsonParameters, ContentType.MULTIPART_FORM_DATA);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("data", stringbody);
		request.setEntity(entity);
		HttpResponse response = null;
		String responseStr = null;
		try {
			response = client.execute(request);
			log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
			responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			log.debug("Payload : {}", responseStr);
		} catch (Exception e) {
			log.error(e.getMessage(), new IllegalStateException(e));
		}
		log.debug("========= Call [{}] End ==========", uri);
		return responseStr;

	}*/

    public String doPostWithJsonResult(String uri, String jsonParameters) {
        log.debug("========= Call [{}] Start ==========", uri);
        log.debug("========= Call [{}] Start ==========", jsonParameters);
        HttpPost request = new HttpPost(uri);
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        request.setConfig(config);
        request.setEntity(new StringEntity(jsonParameters, ContentType.APPLICATION_JSON));
        HttpResponse response = null;
        String responseStr = null;
        try {
            response = client.execute(request);
            log.debug("Response status code: {}", response.getStatusLine().getStatusCode());
            responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            log.debug("Payload : {}", responseStr);
        } catch (Exception e) {
            log.error(e.getMessage(), new IllegalStateException(e));
        }
        log.debug("========= Call [{}] End ==========", uri);
        return responseStr;

    }

    public String doPost(String url, String jsonStr) {
        log.debug("========= Call [{}] Start ==========", url);
        URL u = null;
        HttpURLConnection con = null;

        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            //application/x-www-form-urlencoded
            con.setRequestProperty("content-type", "application/json;charset=UTF-8");
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);
            //con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            if (jsonStr != null && !"".equals(jsonStr)) {
                OutputStreamWriter osw = new OutputStreamWriter(
                        con.getOutputStream(), "UTF-8");
                log.debug("即将发送参数:{}", jsonStr);
                osw.write(jsonStr);
                osw.flush();
                osw.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), new IllegalStateException(e));
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), new IllegalStateException(e));
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = buffer.toString();
        log.debug("Payload: {}", result);
        log.debug("========= Call [{}] End ==========", url);
        return result;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String doPost(String url, String jsonStr, int timeout) {
        setTimeout(timeout);
        return doPost(url, jsonStr);
    }

    public String doGetCustom(String url, int timeout) {
        setTimeout(timeout);
        return doGetCustom(url);
    }


    /**
     * 发送https请求
     *
     * @param
     * @throws Exception
     */
    public static String sendByHttp(Map<String, Object> params, String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> listNVP = new ArrayList<NameValuePair>();
            if (params != null) {
                for (String key : params.keySet()) {
                    listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");

            httpPost.setEntity(entity);
            httpClient = HttpClientUtil.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载证书
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            }).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }

}
