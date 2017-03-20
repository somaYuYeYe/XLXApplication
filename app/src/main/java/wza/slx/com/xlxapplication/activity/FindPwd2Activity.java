package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.utils.CertificateIdUtils;

import static wza.slx.com.xlxapplication.R.id.et_pwd1;
import static wza.slx.com.xlxapplication.R.id.et_pwd2;

/**
 * Created by homelink on 2017/3/19.
 */

public class FindPwd2Activity extends BaseActivity {

    private EditText et_id;
    private EditText et_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd2);

        initView();

    }

    private void initView() {
        et_id = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }

        });
    }

    private void next() {
        String idcard = et_id.getText().toString();
        String errStr = CertificateIdUtils.IDCardValidate(idcard);
        if (!TextUtils.isEmpty(errStr)) {
            Toast.makeText(FindPwd2Activity.this, getString(R.string.toast_idcard_err), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(FindPwd2Activity.this, UpdatePwdActivity.class);
        startActivity(intent);
    }
}
