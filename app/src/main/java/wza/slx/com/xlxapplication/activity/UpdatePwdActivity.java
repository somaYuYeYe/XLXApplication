package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

public class UpdatePwdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePwdActivity.this, UpdatePwdSuccActivity.class);
                startActivity(intent);
            }
        });

    }
}
