package wza.slx.com.xlxapplication.net;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.CallRecord;
import wza.slx.com.xlxapplication.model.Contact;
import wza.slx.com.xlxapplication.model.SmsRecard;
import wza.slx.com.xlxapplication.model.TokenBean;
import wza.slx.com.xlxapplication.net.http.HttpUtil;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.callback.OkCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.utils.LogUtil;


public class NetApi {


    public static void token(Context context, OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        HttpUtil.post(Constant.ROOT + Constant.ACCESSREPORTTOKEN)
                .params(map)
//                .tag(context)
                .enqueue(callback);

    }

    public static void apiWrap(final BaseActivity ac, final Runnable task) {
        final OkCallback<TokenBean> cb = new NoLoadingCallback<TokenBean>(ac, new ModelParser<>(TokenBean.class)) {
            @Override
            public void onSuccess(int code, TokenBean tokenBean) {
                ac.postTask(task);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                Toast.makeText(context, context.getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, String> map = new HashMap<>();
        HttpUtil.post(Constant.ROOT + Constant.ACCESSREPORTTOKEN)
                .params(map)
//                .tag(context)
                .enqueue(cb);

    }
//    public static void token(Activity context, OkCallback callback) {
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        Map<String, String> signMap = Utils.ConvertObjMap2String(map);
//        HttpUtil.get(Constant.TEST_ROOT+Constant.ACCESSREPORTTOKEN)
//                .params(signMap)
//                .tag(context)
//                .enqueue(callback);
//
//    }


    public static void register(final BaseActivity context, final String loginName, final String pwd, final String verifycode,
                                final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map1 = new HashMap<>();
                Map<String, String> map2 = new HashMap<>();
                map1.put("loginName", loginName);
                map1.put("password", pwd);
                map1.put("source", "android");
                map1.put("verifyCode", verifycode);

                LogUtil.i("info", " 1111 == " + SignUtils.payParamsToString(map1, false));

                map2.put("loginName", loginName);
                map2.put("password", pwd);
                map2.put("source", "android");
                map2.put("verifyCode", verifycode);

                map2.put("sign", "" + TripleDES.encrypt(map1));
                map2.put("token", UserManager.getInstance().getToken());

                LogUtil.i("info", " 2222 == " + TripleDES.decrypt(map2.get("sign")));


                LogUtil.i("okhttp", "url = " + Constant.ROOT + Constant.REGISTER + "\n"
                        + SignUtils.payParamsToString(map2, false));

                HttpUtil.post(Constant.ROOT + Constant.REGISTER)
                        .params(map2)
                        .tag(context)
                        .enqueue(callback);
            }
        };

        apiWrap(context, task);

    }

    public static void verifyCode(final BaseActivity context, final String mobile,
                                  final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", mobile);

                HttpUtil.post(Constant.ROOT + Constant.SENDSMSVERIFYCODE)
                        .params(map)
                        .tag(context)
                        .enqueue(callback);

            }
        };

        apiWrap(context, task);

    }

    public static void saveQuestionnaire(final BaseActivity context,
                                         final String credit, final String loan, final String days, final String money, final String retroaction,
                                         final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", UserManager.getInstance().getLoginName());
                map.put("credit", credit);
                map.put("loan", "" + loan); // todo
                map.put("days", "" + days);
                map.put("money", "" + money);
                map.put("retroaction", "" + retroaction);
                LogUtil.i("info", " 1111 == " + SignUtils.payParamsToString(map, false));
                map.put("sign", "" + TripleDES.encrypt(map));
                LogUtil.i("info", " 2222 == " + TripleDES.decrypt(map.get("sign")));
                map.put("token", "" + UserManager.getInstance().getToken());
                LogUtil.i("info", " 全部 == " + SignUtils.payParamsToString(map, false));
                HttpUtil.post(Constant.ROOT + Constant.SAVEQUESTIONNAIRE)
                        .params(map)
                        .tag(context)
                        .enqueue(callback);

            }
        };

        apiWrap(context, task);
    }

    public static void checkLoginName(final BaseActivity context, final String loginName, final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", loginName);

                HttpUtil.get(Constant.ROOT + Constant.CHECKLOGINNAME)
                        .params(map)
                        .tag(context)
                        .enqueue(callback);

            }
        };

        apiWrap(context, task);
    }

    public static void checkIdcard(final BaseActivity context, final String loginName,
                                   final String idcard, final String code, final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", loginName);
                map.put("idCard", idcard);
                map.put("verifyCode", code);

                HttpUtil.post(Constant.ROOT + Constant.CHECKIDCARD)
                        .params(map)
                        .tag(context)
                        .enqueue(callback);

            }
        };

        apiWrap(context, task);

    }

    public static void updatepwd(final BaseActivity context, final String loginName, final String idcard, final String code,
                                 final String newPwd, final String confimPwd,
                                 final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", loginName);
                map.put("idCard", idcard);
                map.put("verifyCode", code);
                map.put("newPwd", newPwd);
                map.put("confimPwd", confimPwd);

                HttpUtil.post(Constant.ROOT + Constant.UPDATEPWD)
                        .params(map)
                        .tag(context)
                        .enqueue(callback);

            }
        };

        apiWrap(context, task);
    }

    public static void uploadSms(Context context, final List<SmsRecard> data, final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", UserManager.getInstance().getLoginName());
                map.put("token", UserManager.getInstance().getToken());
//                map.put("smsRecords", data.toString());
//                map.put("smsRecords", String.valueOf(data));
//                map.put("smsRecords", JSON.toJSON(data).toString());

                JSONArray arr = new JSONArray();
                for (SmsRecard s : data) {
                    JSONObject b = new JSONObject();
                    b.put("addresss", s.addresss);
                    b.put("contect", s.contect);
                    b.put("sendTime", s.sendTime);
                    b.put("type", s.type);
                    arr.add(b);
                }

                LogUtil.i("info", " upload sms == " + SignUtils.payParamsToString(map, false));

                HttpUtil.post(Constant.ROOT + Constant.smsRecords)
                        .params(map)
                        .enqueue(callback);
            }
        };

        uploadWrap(context, task);

    }

    public static void uploadContact(Context context, final List<Contact> data, final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", UserManager.getInstance().getLoginName());
                map.put("token", UserManager.getInstance().getToken());
                map.put("contacts", JSON.toJSON(data).toString());

                LogUtil.i("info", " upload contacts == " + SignUtils.payParamsToString(map, false));

                HttpUtil.post(Constant.ROOT + Constant.contacts)
                        .params(map)
                        .enqueue(callback);
            }
        };

        uploadWrap(context, task);

    }

    public static void uploadCallRecord(Context context, final List<CallRecord> data, final OkCallback callback) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("loginName", UserManager.getInstance().getLoginName());
                map.put("token", UserManager.getInstance().getToken());
                map.put("callRecords", JSON.toJSON(data).toString());

                LogUtil.i("info", " upload callRecords == " + SignUtils.payParamsToString(map, false));

                HttpUtil.post(Constant.ROOT + Constant.callRecords)
                        .params(map)
                        .enqueue(callback);
            }
        };

        uploadWrap(context, task);

    }

    public static void uploadWrap(Context context, final Runnable task) {

        final OkCallback<TokenBean> cb = new NoLoadingCallback<TokenBean>(context, new ModelParser<>(TokenBean.class)) {
            @Override
            public void onSuccess(int code, TokenBean tokenBean) {
                task.run();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                Toast.makeText(context, context.getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, String> map = new HashMap<>();
        HttpUtil.post(Constant.ROOT + Constant.ACCESSREPORTTOKEN)
                .params(map)
                .enqueue(cb);


    }


}
