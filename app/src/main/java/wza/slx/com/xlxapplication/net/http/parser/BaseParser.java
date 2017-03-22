package wza.slx.com.xlxapplication.net.http.parser;

import okhttp3.Response;

public abstract class BaseParser<T> {

    protected int code;

    protected abstract T parse(Response response) throws Exception;

    public T parseResponse(Response response) throws Exception {
        code = response.code();
        return parse(response);
    }

    public int getCode() {
        return code;
    }

}
