package wza.slx.com.xlxapplication.net.http.parser;

import okhttp3.Response;

/**
 * Created by huangjin on 2016/8/27.
 * 字符串自动解析
 */
public class StringParser extends BaseParser<String> {

    @Override
    protected String parse(Response response) throws Exception {
        if(response.isSuccessful()){
            return response.body().string();
        }
        return null;
    }
}
