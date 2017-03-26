package wza.slx.com.xlxapplication.net.http.parser;

import okhttp3.Response;
import wza.slx.com.xlxapplication.utils.LogUtil;

public abstract class BaseParser<T> {

    protected int code;

    protected abstract T parse(Response response) throws Exception;

    public T parseResponse(Response response) throws Exception {
        code = response.code();
        LogUtil.i("http", " code = " + code);
        return parse(response);
    }

    public int getCode() {
        return code;
    }

}
