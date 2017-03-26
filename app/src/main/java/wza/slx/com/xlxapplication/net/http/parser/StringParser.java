package wza.slx.com.xlxapplication.net.http.parser;

import okhttp3.Response;
import wza.slx.com.xlxapplication.net.TripleDES;
import wza.slx.com.xlxapplication.utils.LogUtil;

/**
 符串自动解析
 */
public class StringParser extends BaseParser<String> {

    @Override
    protected String parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            String body = response.body().string();
            LogUtil.i("http parse", "body 1 = " + body);

//            LogUtil.i("http parse", "body 2 = " + TripleDES.decrypt(body));

            return body;
        }
        return null;
    }
}
