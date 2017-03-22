package wza.slx.com.xlxapplication.net.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangjin on 2016/8/26.
 *
 */
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Log.e("OKHttp","==="+request.url());
        Response response = chain.proceed(request);
        Log.e("OKHttp","==="+response.code()+":"+response.message());
        Log.e("OKHttp","==="+response.peekBody(1024).string());

        return response;
    }
}
