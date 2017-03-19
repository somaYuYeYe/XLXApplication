package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

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

    private boolean pwdVisi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

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
                    pwdVisi = false;
                } else {
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwdVisi = true;
                }
                break;
            case R.id.tv_login:
                toQuestion();
                break;
            case R.id.tv_get_code:
                break;
            case R.id.tv_forget_pwd:
                toFindpwd();
                break;
        }
    }

    private void toQuestion() {
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }

    private void toFindpwd() {
        Intent intent = new Intent(this, FindPwd1Activity.class);
        startActivity(intent);
    }
}
