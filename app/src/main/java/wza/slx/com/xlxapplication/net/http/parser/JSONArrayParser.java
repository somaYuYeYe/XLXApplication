package wza.slx.com.xlxapplication.net.http.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import okhttp3.Response;

/**
 * Created by huangjin on 2016/8/27.
 * 
 */
public class JSONArrayParser extends BaseParser<JSONArray> {

    @Override
    protected JSONArray parse(Response response) throws Exception {
        if(response.isSuccessful()){
            JSONArray jsonArray = JSON.parseArray(response.body().string());
            return jsonArray;
        }
        return null;
    }
}
