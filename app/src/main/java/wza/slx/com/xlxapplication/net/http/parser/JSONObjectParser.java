package wza.slx.com.xlxapplication.net.http.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Response;

/**
 * Created by huangjin on 2016/8/27.
 * 简单jsonObject自动解析
 */
public class JSONObjectParser extends BaseParser<JSONObject> {

    @Override
    protected JSONObject parse(Response response) throws Exception {
        if(response.isSuccessful()){
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            return jsonObject;
        }
        return null;
    }
}
