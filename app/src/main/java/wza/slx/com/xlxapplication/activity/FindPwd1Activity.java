package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.FieldPacker;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.model.CommonBean;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.LoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.utils.Utils;

/**
 * Created by homelink on 2017/3/19.
 */

public class FindPwd1Activity extends BaseActivity {

    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd1);

        et_phone = (EditText) findViewById(R.id.et_phone);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phone = et_phone.getText().toString();
                if (!Utils.isMobile(phone)) {
                    Toast.makeText(FindPwd1Activity.this, getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();
                    return;
                }

                NetApi.checkLoginName(FindPwd1Activity.this, phone,
                        new LoadingCallback<CommonBean>(FindPwd1Activity.this, new ModelParser<CommonBean>(CommonBean.class)) {
                            @Override
                            public void onSuccess(int code, CommonBean commonBean) {
                                super.onSuccess(code, commonBean);
//                                if ("0000".equals(commonBean.code)) {
                                Toast.makeText(FindPwd1Activity.this, commonBean.msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FindPwd1Activity.this, FindPwd2Activity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
//                                } else {
//                                    Toast.makeText(FindPwd1Activity.this, commonBean.msg, Toast.LENGTH_SHORT).show();
//
//                                }
                            }
                        });

            }
        });

    }
}
