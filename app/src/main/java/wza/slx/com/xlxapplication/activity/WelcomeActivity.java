package wza.slx.com.xlxapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.TokenBean;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.service.SlxIntentService;
import wza.slx.com.xlxapplication.utils.LogUtil;
import wza.slx.com.xlxapplication.utils.ScreenUtil;
import wza.slx.com.xlxapplication.utils.Utils;

import static android.R.attr.dashGap;
import static android.R.attr.dial;
import static android.R.attr.id;

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

        Utils.checkPermis(this, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            for (String perm : permissions) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, perm);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //弹出对话框接收权限
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    Dialog dialog;
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.setCancelable(false);
                    dialog.findViewById(R.id.layout).getLayoutParams().width = ScreenUtil.getScreenWidth(this)
                            * 2 / 3;
                    TextView tv = (TextView) dialog.findViewById(R.id.msg);
                    tv.setText("为了方便您的使用，请给我们授权。您可以在权限管理中设置。");
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            finish();
                        }
                    }, 3500);

                    return;
                } else {
                    Log.i("info check per", "have permiss = " + perm + "  | " + Arrays.asList(permissions).toString());
                }
            }

            Utils.upload(this);
        }

    }
}
