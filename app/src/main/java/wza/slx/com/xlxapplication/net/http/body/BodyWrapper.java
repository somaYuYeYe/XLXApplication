package wza.slx.com.xlxapplication.net.http.body;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import wza.slx.com.xlxapplication.net.http.callback.OkProgressCallback;

public class BodyWrapper {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final OkProgressCallback progressCallback) {
        return client.newBuilder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ResponseProgressBody(originalResponse.body(), progressCallback))
                                .build();
                    }
                }).build();
    }

    public static RequestProgressBody addProgressRequestListener(RequestBody requestBody, OkProgressCallback progressCallback) {
        return new RequestProgressBody(requestBody, progressCallback);
    }
}