package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_pwd1;
    private EditText et_pwd2;

    private ImageView iv_pwd_switch1;
    private boolean pwdVisi1 = false;
    private ImageView iv_pwd_switch2;
    private boolean pwdVisi2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }

        });

        et_pwd1 = (EditText) findViewById(R.id.et_pwd1);
        et_pwd2 = (EditText) findViewById(R.id.et_pwd2);
        iv_pwd_switch1 = (ImageView) findViewById(R.id.iv_pwd_switch1);
        iv_pwd_switch2 = (ImageView) findViewById(R.id.iv_pwd_switch2);

        iv_pwd_switch1.setOnClickListener(this);
        iv_pwd_switch2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pwd_switch1:
                if (pwdVisi1) {
                    et_pwd1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_pwd_switch1.setImageResource(R.mipmap.ic_see);
                    pwdVisi1 = false;
                } else {
                    et_pwd1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_pwd_switch1.setImageResource(R.mipmap.ic_close);
                    pwdVisi1 = true;
                }
                break;
            case R.id.iv_pwd_switch2:
                if (pwdVisi2) {
                    et_pwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_pwd_switch2.setImageResource(R.mipmap.ic_see);
                    pwdVisi2 = false;
                } else {
                    et_pwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_pwd_switch2.setImageResource(R.mipmap.ic_close);
                    pwdVisi2 = true;
                }
                break;
        }
    }
    private void next() {
        String pwd1 = et_pwd1.getText().toString();
        String pwd2 = et_pwd2.getText().toString();
        if (TextUtils.isEmpty(pwd1) || pwd1.length() < 6 || pwd1.length() > 20) {
            Toast.makeText(this, getString(R.string.toast_pwd_err), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(this, getString(R.string.toast_pwd_err), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(UpdatePwdActivity.this, UpdatePwdSuccActivity.class);
        startActivity(intent);
    }
}
