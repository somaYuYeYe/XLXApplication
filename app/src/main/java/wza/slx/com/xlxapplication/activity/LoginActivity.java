package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import wza.slx.com.xlxapplication.BuildConfig;
import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.App;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.manager.UserManager;
import wza.slx.com.xlxapplication.model.CommonBean;
import wza.slx.com.xlxapplication.model.TokenBean;
import wza.slx.com.xlxapplication.net.Constant;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.LoadingCallback;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.net.http.parser.StringParser;
import wza.slx.com.xlxapplication.utils.LogUtil;
import wza.slx.com.xlxapplication.utils.StreamUtil;
import wza.slx.com.xlxapplication.utils.Utils;

/**
 * Created by homelink on 2017/3/19.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_code;

    private TextView tv_login;
    private TextView tv_get_code;
    private TextView tv_forget_pwd;

    private ImageView iv_phone_del;
    private ImageView iv_pwd_switch;

    private CheckBox cb;

    private boolean pwdVisi = false;

    private Timer time;
    private TimerTask task;
    private int count = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what < 0) {
                task.cancel();
                time.cancel();
                tv_get_code.setText("获取验证码");
                tv_get_code.setEnabled(true);
            } else {
                tv_get_code.setText("");
                tv_get_code.setHint(msg.what + "秒后重新获取");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        NetApi.token(this, new NoLoadingCallback<TokenBean>(LoginActivity.this, new ModelParser<TokenBean>(TokenBean.class)) {
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
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_code = (EditText) findViewById(R.id.et_code);

        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);

        iv_phone_del = (ImageView) findViewById(R.id.iv_phone_del);
        iv_pwd_switch = (ImageView) findViewById(R.id.iv_pwd_switch);

        cb = (CheckBox) findViewById(R.id.cb);

        iv_phone_del.setOnClickListener(this);
        iv_pwd_switch.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phone_del:

                et_phone.setText("");
                break;
            case R.id.iv_pwd_switch:

                if (pwdVisi) {
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_pwd_switch.setImageResource(R.mipmap.ic_see);
                    pwdVisi = false;
                } else {
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_pwd_switch.setImageResource(R.mipmap.ic_close);
                    pwdVisi = true;
                }
                break;
            case R.id.tv_login:
                toQuestion();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_forget_pwd:
                toFindpwd();
                break;
        }
    }

    private void getCode() {
        String mobile = et_phone.getText().toString();
        if (!Utils.isMobile(mobile)) {
            Toast.makeText(this, getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();
            return;
        }
        tv_get_code.setEnabled(false);
        tv_get_code.setText("");
        tv_get_code.setHint(count + "秒后重新获取");

        count = 60;
        if (time == null) {
            time = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    count--;
                    handler.sendEmptyMessage(count);
                }
            };
        }
        time.schedule(task, 1000, 1000);

        NetApi.verifyCode(this, mobile, new NoLoadingCallback<String>(this, new StringParser()) {
            @Override
            public void onSuccess(int code, String s) {
                LogUtil.i("info", " s = " + s);
            }
        });
    }

    private void toQuestion() {

//        Utils.checkPermis(this, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,
//                Manifest.permission.READ_SMS);
//
//        Utils.getPhoneContacts(this);
//
//        Utils.getSIMContacts(this); // null
//
//        String sms = Utils.getSmsInPhone(this);
//        Log.i("info", "---- " + sms);
//
//        String calllog = Utils.getCallHistoryList(this, this.getContentResolver());
//        Log.i("info", "calls history == " + calllog);
//


        final String phone = et_phone.getText().toString();
        if (!Utils.isMobile(phone)) {
            Toast.makeText(this, getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 20) {
            Toast.makeText(this, getString(R.string.toast_pwd_err), Toast.LENGTH_SHORT).show();
            return;
        }

        String code = et_code.getText().toString();
        if (TextUtils.isEmpty(code) || code.length() < 6) {
            Toast.makeText(this, getString(R.string.toast_code_err), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cb.isChecked()) {
            Toast.makeText(this, getString(R.string.toast_cb_err), Toast.LENGTH_SHORT).show();
            return;
        }

//        Intent intent = new Intent(this, QuestionActivity.class);
//        startActivity(intent);

//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(true);
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
//        dialog.show();

        NetApi.register(this, phone, pwd, code,
                new LoadingCallback<CommonBean>(this, new ModelParser<CommonBean>(CommonBean.class)) {
                    @Override
                    public void onSuccess(int code, CommonBean s) {
                        super.onSuccess(code, s);
                        LogUtil.i("http register", "succ = " + s);
                        if (BuildConfig.LOG_DEBUG) {
//                            if ("0000".equals(s.code)) {
                                UserManager.getInstance().setLoginName(phone);
                                Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                                startActivity(intent);
                                Utils.upload(App.instance);
//                            } else {
                                Toast.makeText(LoginActivity.this, s.msg, Toast.LENGTH_SHORT).show();
//                            }

                        } else {
                            if ("0000".equals(s.code)) {
                                UserManager.getInstance().setLoginName(phone);
                                Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                                startActivity(intent);
                                Utils.upload(App.instance);
                            } else {
                                Toast.makeText(LoginActivity.this, s.msg, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);
                        LogUtil.i("http fail", " fail ----------");
                    }
                });


    }

    private void toFindpwd() {
        Intent intent = new Intent(this, FindPwd1Activity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0, size = permissions.length; i < size; i++) {
            Log.i("info onRPR ", i + " " + permissions[i] + " " + grantResults);
        }
    }
}
