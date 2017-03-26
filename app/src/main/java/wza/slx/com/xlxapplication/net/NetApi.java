package wza.slx.com.xlxapplication.net;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.net.http.HttpUtil;
import wza.slx.com.xlxapplication.net.http.callback.OkCallback;
import wza.slx.com.xlxapplication.utils.LogUtil;


public class NetApi {


    public static void token(Context context, OkCallback callback) {

        Map<String, String> map = new HashMap<>();


        HttpUtil.get(Constant.ROOT + Constant.ACCESSREPORTTOKEN)
                .params(map)
//                .tag(context)
                .enqueue(callback);

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


    public static void register(Activity context, String loginName, String pwd, String verifycode,
                                OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("password", pwd);
        map.put("source", "android");
        map.put("verifyCode", verifycode);
        map.put("token", UserManager.getInstance().getToken());
        map.put("sign", "");

        LogUtil.i("okhttp", "url = " + Constant.ROOT + Constant.REGISTER + "\n"
                + SignUtils.payParamsToString(map, false));

        HttpUtil.get(Constant.ROOT + Constant.REGISTER)
                .params(map)
                .tag(context)
                .enqueue(callback);

    }

    public static void verifyCode(Activity context, String mobile,
                                  OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
//        map.put("password", "");
//        map.put("source", "");
//        map.put("verifyCode", "");
//        map.put("token", "");
//        map.put("sign", "");

        HttpUtil.get(Constant.ROOT + Constant.SENDSMSVERIFYCODE)
                .params(map)
                .tag(context)
                .enqueue(callback);

    }

    public static void saveQuestionnaire(Activity context,
                                         String credit, String loan, String days, String money, String retroaction,
                                         OkCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put("loginName", UserManager.getInstance().getLoginName());
        map.put("credit", credit);
        map.put("loan", "" + loan); // todo
        map.put("days", "" + days);
        map.put("money", "" + money);
        map.put("retroaction", "" + retroaction);

        map.put("token", "" + UserManager.getInstance().getToken());
        map.put("sign", "");

        HttpUtil.get(Constant.ROOT + Constant.SAVEQUESTIONNAIRE)
                .params(map)
                .tag(context)
                .enqueue(callback);
    }

    public static void checkLoginName(Activity context, String loginName, OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);

        HttpUtil.get(Constant.ROOT + Constant.CHECKLOGINNAME)
                .params(map)
                .tag(context)
                .enqueue(callback);
    }

    public static void checkIdcard(Activity context, String loginName, String idcard, String code, OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("idCard", idcard);
        map.put("verifyCode", code);

        HttpUtil.get(Constant.ROOT + Constant.CHECKIDCARD)
                .params(map)
                .tag(context)
                .enqueue(callback);
    }

    public static void updatepwd(Activity context, String loginName, String idcard, String code,
                                 String newPwd, String confimPwd,
                                 OkCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("idCard", idcard);
        map.put("verifyCode", code);
        map.put("newPwd", newPwd);
        map.put("confimPwd", confimPwd);

        HttpUtil.get(Constant.ROOT + Constant.UPDATEPWD)
                .params(map)
                .tag(context)
                .enqueue(callback);
    }

}
