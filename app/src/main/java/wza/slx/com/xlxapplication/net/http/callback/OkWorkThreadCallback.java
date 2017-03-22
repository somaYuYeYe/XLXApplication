package wza.slx.com.xlxapplication.net.http.callback;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import wza.slx.com.xlxapplication.net.http.exception.HttpException;
import wza.slx.com.xlxapplication.net.http.parser.BaseParser;

/**
 * http请求工作线程回调
 * @param <T>
 */
public abstract class OkWorkThreadCallback<T> implements Callback {


    private BaseParser<T> mParser;

    public OkWorkThreadCallback(BaseParser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        onFailure(e);
    }

    @Override
    public void onResponse(Call call,final Response response) {
        try {
            final T t = mParser.parseResponse(response);
            final int code = mParser.getCode();
            if (response.isSuccessful()) {
                onSuccess(code, t);
            } else {
                onFailure(new HttpException(code));
            }
        } catch (final Exception e) {
            onFailure(e);
        }
    }

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(Throwable e);


}