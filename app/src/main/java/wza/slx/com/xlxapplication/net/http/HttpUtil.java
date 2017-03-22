package wza.slx.com.xlxapplication.net.http;



import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import wza.slx.com.xlxapplication.net.http.builder.GetRequestBuilder;
import wza.slx.com.xlxapplication.net.http.builder.PostRequestBuilder;
import wza.slx.com.xlxapplication.net.http.builder.PutRequestBuilder;
import wza.slx.com.xlxapplication.net.http.interceptor.LogInterceptor;
import wza.slx.com.xlxapplication.net.http.interceptor.MockInterceptor;

/**
 * Created by huangjin on 2016/8/24.
 *
 */
public class HttpUtil {


    private static OkHttpClient httpClient;

    private static OkHttpClient.Builder httpClientBuilder;

    private static OkHttpClient init() {
        synchronized (HttpUtil.class) {
            if (httpClient == null) {
                if(httpClientBuilder == null){
                    httpClientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS);
                }
                httpClient = httpClientBuilder.build();
            }
        }
        return httpClient;
    }

    public static OkHttpClient getInstance() {
        return httpClient == null ? init() : httpClient;
    }

    public static OkHttpClient.Builder getInstanceBuilder() {
        if(httpClientBuilder == null){
            httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
        }
        return httpClientBuilder;
    }

    public static void setInstance(OkHttpClient okHttpClient) {
        HttpUtil.httpClient = okHttpClient;
    }

    public static GetRequestBuilder get(String url) {
        return new GetRequestBuilder().url(url);
    }

    public static PostRequestBuilder post(String url) {
        return new PostRequestBuilder().url(url);
    }

    public static PutRequestBuilder put(String url){
        return new PutRequestBuilder().url(url);
    }

    public static void cancel(Object tag) {
        Dispatcher dispatcher = getInstance().dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static void isLog(boolean isLog){
        if(isLog){
            getInstanceBuilder().addInterceptor(new LogInterceptor());
        }
    }



    public static void isMock(boolean isMock,File mockFileDir){
        if(isMock && mockFileDir != null){
            try {
                if(!mockFileDir.exists() && mockFileDir.mkdirs()){

                }
                getInstanceBuilder().addInterceptor(new MockInterceptor(mockFileDir));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
