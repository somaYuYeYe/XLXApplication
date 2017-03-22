package wza.slx.com.xlxapplication.net.http.builder;

import android.text.TextUtils;


import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import wza.slx.com.xlxapplication.net.http.callback.OkCallback;
import wza.slx.com.xlxapplication.net.http.callback.OkProgressCallback;

/**
 * get请求构造器
 */
public class GetRequestBuilder extends RequestBuilder {



    /**
     * 添加response进度回调，下载进度
     * @param rspProgressCallback 进度回调
     * @return GetRequestBuilder
     */
    public GetRequestBuilder addRespProgressCallback(OkProgressCallback rspProgressCallback){
        this.rspProgressCallback = rspProgressCallback;
        return this;
    }

    /**
     * 设置get请求的url
     * @param url
     * @return
     */
    @Override
    public GetRequestBuilder url(String url) {
        super.url(url);
        return this;
    }


    @Override
    public GetRequestBuilder params(Map<String, String> params) {
        super.params(params);
        return this;
    }

    @Override
    public GetRequestBuilder addParam(String key, String value) {
        super.addParam(key,value);
        return this;
    }

    @Override
    public GetRequestBuilder headers(Map<String, String> headers) {
        super.headers(headers);
        return this;
    }

    @Override
    public GetRequestBuilder addHeader(String key, String values) {
        super.addHeader(key, values);
        return this;
    }

    public GetRequestBuilder setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public GetRequestBuilder setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    @Override
    public GetRequestBuilder tag(Object tag) {
        super.tag(tag);
        return this;
    }

    @Override
    public Call enqueue(Callback callback) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        if (params != null && params.size() > 0) {
            url = appendParams(url, params);
        }

        Request.Builder builder = new Request.Builder().url(url);

        if (tag != null) {
            builder.tag(tag);
        }

        appendHeaders(builder, headers);

        Request request = builder.build();

        if (callback instanceof OkCallback) {
            ((OkCallback) callback).onStart();
        }

        Call call = cloneClient().newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public Response execute() throws IOException {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        if (params != null && params.size() > 0) {
            url = appendParams(url, params);
        }

        Request.Builder builder = new Request.Builder().url(url);

        if (tag != null) {
            builder.tag(tag);
        }

        appendHeaders(builder, headers);

        Request request = builder.build();

        Call call = cloneClient().newCall(request);
        return call.execute();
    }

}
