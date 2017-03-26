package wza.slx.com.xlxapplication.net.http.parser;

import com.alibaba.fastjson.JSON;

import okhttp3.Response;
import wza.slx.com.xlxapplication.utils.LogUtil;

/**
 * Created by huangjin on 2016/9/5.
 * 简单model类自动解析
 */
public class ModelParser<T> extends BaseParser<T> {
    protected Class<T> clazz;

    public ModelParser(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    protected T parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            LogUtil.i("http body ", " body = " + response.body().string());
            T object = JSON.parseObject(response.body().string(), clazz);
            return object;
        }
        return null;
    }
}