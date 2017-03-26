package wza.slx.com.xlxapplication.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import wza.slx.com.xlxapplication.base.App;
import wza.slx.com.xlxapplication.model.TokenBean;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.utils.LogUtil;

/**
 * Created by homelink on 2017/3/26.
 */

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private String token;
    private String loginName; // 登录的电话号码

    /**
     *  登录电话号码
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     *  登录的电话号码
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void getTokenFromNet() {

        NetApi.token(App.instance, new NoLoadingCallback<TokenBean>(App.instance,
                new ModelParser<TokenBean>(TokenBean.class)) {
            @Override
            public void onSuccess(int code, TokenBean tokenBean) {
                LogUtil.i("info", "=== ==== === " + tokenBean.toString());
                UserManager.getInstance().setToken(tokenBean.token);
                mHandler.removeMessages(1);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                mHandler.sendEmptyMessageDelayed(1, 5000);
            }
        });
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getTokenFromNet();
        }
    };
}
