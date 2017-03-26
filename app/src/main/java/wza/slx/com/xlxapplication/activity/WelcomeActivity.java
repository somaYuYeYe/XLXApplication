package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.TokenBean;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.utils.LogUtil;

public class WelcomeActivity extends BaseActivity {

    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        NetApi.token(this, new NoLoadingCallback<TokenBean>(WelcomeActivity.this, new ModelParser<TokenBean>(TokenBean.class)) {
            @Override
            public void onSuccess(int code, TokenBean tokenBean) {
                LogUtil.i("info", "=== ==== === " + tokenBean.toString());
                UserManager.getInstance().setToken(tokenBean.token);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                UserManager.getInstance().getTokenFromNet();
            }
        });

    }

    private void initView() {
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/3/18
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
