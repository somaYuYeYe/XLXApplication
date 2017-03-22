package wza.slx.com.xlxapplication.net.http.callback;

import android.os.Handler;
import android.os.Looper;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import wza.slx.com.xlxapplication.net.http.exception.HttpException;
import wza.slx.com.xlxapplication.net.http.parser.BaseParser;

/**
 * http请求UI线程回调
 * @param <T>
 */
public abstract class OkCallback<T> implements Callback {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private BaseParser<T> mParser;

    public OkCallback(BaseParser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(e);
            }
        });
    }

    @Override
    public void onResponse(Call call,final Response response) {
        try {
            final T t = mParser.parseResponse(response);
            final int code = mParser.getCode();
            if (response.isSuccessful()) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(code, t);
                    }
                });
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(new HttpException(code));
                    }
                });
            }
        } catch (final Exception e) {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(e);
                }
            });
        }
    }

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(Throwable e);

    public void onStart() {
    }

}