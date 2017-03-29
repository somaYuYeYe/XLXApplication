package wza.slx.com.xlxapplication.net.http.parser;

import com.alibaba.fastjson.JSON;

import java.net.URLDecoder;

import okhttp3.Response;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.CommonBean;
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
            // response.body().string() 只能执行一次
            String result = response.body().string();
            URLDecoder.decode(result, "utf-8");
            LogUtil.i("http body ", " body = " + URLDecoder.decode(result, "utf-8"));
            T object = JSON.parseObject(result, clazz);
            return object;
        }
        return null;
    }
}