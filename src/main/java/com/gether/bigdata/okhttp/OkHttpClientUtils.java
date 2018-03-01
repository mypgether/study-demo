package com.gether.bigdata.okhttp;

import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/8/10.
 */
public class OkHttpClientUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtils.class);

    public static final int CORE_POLL_SIZE = 800;
    public static final int MAX_POLL_SIZE = 200;
    public static final long KEEP_ALIVE_TIME = 60;

    private static final BlockingQueue QUEUE = new LinkedBlockingQueue<Runnable>(1000);

    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(CORE_POLL_SIZE, MAX_POLL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, QUEUE, Util.threadFactory("OkHttp Dispatcher", false));

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .dispatcher(new Dispatcher(executorService))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static String get(String url) throws HttpRequestException {
        Request request = createGetRequest(url);
        Response response = execute(request);
        try {
            return convertResponseBodyToString(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static byte[] getBytes(String url) throws HttpRequestException {
        Request request = createGetRequest(url);
        Response response = execute(request);
        try {
            return convertResponseBodyToBytes(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static String postWithBody(String url, String json) throws HttpRequestException {
        Request request = createPostBody(url, json);
        Response response = execute(request);
        try {
            return convertResponseBodyToString(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static String postWithBody(String url, String json, long timeout, TimeUnit timeUnit) throws HttpRequestException {
        Request request = createPostBody(url, json);
        OkHttpClient okHttpClient = client.newBuilder().readTimeout(timeout, timeUnit).connectTimeout(timeout, timeUnit).writeTimeout(timeout, timeUnit).build();
        Response response = execute(okHttpClient, request);
        try {
            return convertResponseBodyToString(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static String post(String url, Map<String, String> params) throws HttpRequestException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = execute(request);
        try {
            return convertResponseBodyToString(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static String post(String url, Map<String, String> params, long timeout, TimeUnit timeUnit) throws HttpRequestException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();

        OkHttpClient okHttpClient = client.newBuilder().readTimeout(timeout, timeUnit).connectTimeout(timeout, timeUnit).writeTimeout(timeout, timeUnit).build();
        Response response = execute(okHttpClient, request);
        try {
            return convertResponseBodyToString(response.body());
        } catch (IOException e) {
            logger.error("OkHttp getAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp getAsBytes error, %s", e.getMessage()));
        }
    }

    public static byte[] postWithBodyBytes(String url, String json) throws HttpRequestException {
        Request request = createPostBody(url, json);
        Response response = execute(request);
        try {
            return convertResponseBodyToBytes(response.body());
        } catch (IOException e) {
            logger.error("OkHttp postAsBytes error", e);
            throw new HttpRequestException(String.format("OkHttp postAsBytes error, %s", e.getMessage()));
        }
    }

    private static Response execute(Request request) throws HttpRequestException {
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            logger.error("OkHttp post error", e);
            throw new HttpRequestException(String.format("OkHttp post error, %s", e.getMessage()));
        }
    }

    private static Response execute(OkHttpClient okHttpClient, Request request) throws HttpRequestException {
        try {
            return okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            logger.error("OkHttp post error", e);
            throw new HttpRequestException(String.format("OkHttp post error, %s", e.getMessage()));
        }
    }

    private static String convertResponseBodyToString(ResponseBody responseBody) throws IOException {
        if (responseBody != null) {
            return responseBody.string();
        }
        return null;
    }

    private static byte[] convertResponseBodyToBytes(ResponseBody responseBody) throws IOException {
        if (responseBody != null) {
            return responseBody.bytes();
        }
        return null;
    }

    private static Request createPostBody(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }

    private static Request createGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }
}