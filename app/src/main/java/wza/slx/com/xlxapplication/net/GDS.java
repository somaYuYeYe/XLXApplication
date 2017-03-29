package wza.slx.com.xlxapplication.net;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.wlf.common.httpclient.HttpClientUtil;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.telecom.Call;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.List;

import wza.slx.com.xlxapplication.manager.TypeEva;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.CallRecord;
import wza.slx.com.xlxapplication.model.Contact;
import wza.slx.com.xlxapplication.model.SmsRecard;
import wza.slx.com.xlxapplication.utils.LogUtil;

//import org.apache.http.client.methods.CloseableHttpResponse;

public class GDS {

    private static final String APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private List data;
    private GDSCallback callback;

    public GDS(List data, GDSCallback callback) {
        this.data = data;
        this.callback = callback;
    }


    public void upload(@TypeEva.EvaType final int type) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                doinback(type);
                return null;
            }
        }.execute();
    }


    private void doinback(@TypeEva.EvaType int type) {
//    	String validateURL = "http://chat.wanlefu.com/wlf-weChat-web/appApi/accessReportToken"; //http://chat.wanlefu.com/wlf-weChat-web/
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		String result = HttpClientUtil.post(validateURL, nameValuePairs);
//		System.out.println(result);
//
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, Object> mapMo = null;
//		try {
//			mapMo = mapper.readValue(result,Map.class);
//		}catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		System.out.println(mapMo.get("token"));
        int reCode = -1;
        try {
            JSONObject root = new JSONObject();
            root.put("token", UserManager.getInstance().getToken());
            root.put("loginName", UserManager.getInstance().getLoginName());

            JSONArray arr = new JSONArray();
            switch (type) {
                case TypeEva.SMS:
                    int size1 = data.size();
                    for (int i = 0; i < size1; i++) {
                        SmsRecard s = (SmsRecard) data.get(i);
                        JSONObject b = new JSONObject();
                        b.put("addresss", s.addresss);
                        b.put("contect", s.contect);
                        b.put("sendTime", s.sendTime);
                        b.put("type", s.type);
                        arr.add(b);
                    }

                    break;
                case TypeEva.CallRecords:
                    int size2 = data.size();
                    for (int i = 0; i < size2; i++) {
                        CallRecord s = (CallRecord) data.get(i);
                        JSONObject b = new JSONObject();
                        b.put("callName", s.callName);
                        b.put("callTime", s.callTime);
                        b.put("phone", s.phone);
                        b.put("callDuration", s.callDuration);
                        b.put("type", s.type);
                        arr.add(b);
                    }

                    break;
                case TypeEva.Contacts:
                    int size3 = data.size();
                    for (int i = 0; i < size3; i++) {
                        Contact s = (Contact) data.get(i);
                        JSONObject b = new JSONObject();
                        b.put("callName", s.callName);
                        b.put("phone", s.phone);
                        arr.add(b);
                    }

                    break;
            }


//            JSONObject node = new JSONObject();
//            node.put("callName", "张三");
//            node.put("phone", "18888888888");
//            node.put("type", "1");
//            node.put("callTime", "1");
//            node.put("callDuration", "1");
//            arr.put(node);
//            JSONObject node2 = new JSONObject();
//            node2.put("callName", "李四");
//            node2.put("phone", "18899999999");
//            node2.put("type", "1");
//            node2.put("callTime", "1");
//            node2.put("callDuration", "1");
//            arr.put(node2);

            String noteName = getDataNoteName(type);

            root.put(noteName, arr);
            String json = root.toJSONString();

            String URLSTR = getUrl(type);

            // 将JSON进行UTF-8编码,以便传输中文
            String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URLSTR);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

            StringEntity se = new StringEntity(encoderJson);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(se);
            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(httpPost);
            // response实体
            StatusLine statusline = httpResponse.getStatusLine();
            reCode = statusline.getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String str = EntityUtils.toString(entity, "UTF-8");
                System.out.println("-- gds  " + str + "  url " + URLSTR);
                if (callback != null) {
                    callback.onSucc(str);
                }
                return;
            }
        } catch (Exception e) {
            LogUtil.e("GDS exception ", "code = " + reCode + " e = " + e.getMessage());
            if (callback != null) {
                callback.onFail(reCode, e);
            }
        } finally {
//			httpResponse.close();
        }
    }

    @NonNull
    private String getUrl(@TypeEva.EvaType int type) {
        String noteName = Constant.callRecords;
        switch (type) {
            case TypeEva.CallRecords:
                noteName = Constant.callRecords;
                break;
            case TypeEva.Contacts:
                noteName = Constant.contacts;
                break;
            case TypeEva.SMS:
                noteName = Constant.smsRecords;
                break;
        }
        return Constant.ROOT + noteName;
    }

    @NonNull
    private String getDataNoteName(@TypeEva.EvaType int type) {
        String noteName = "callRecords";
        switch (type) {
            case TypeEva.CallRecords:
                noteName = "callRecords";
                break;
            case TypeEva.Contacts:
                noteName = "contacts";
                break;
            case TypeEva.SMS:
                noteName = "smsRecords";
                break;
        }
        return noteName;
    }

    public interface GDSCallback {
        void onSucc(String result);

        void onFail(int code, Exception e);
    }
}