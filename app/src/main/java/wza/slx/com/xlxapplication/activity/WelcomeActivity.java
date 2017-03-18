package wza.slx.com.xlxapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {
    
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/3/18
            }
        });
    }
}
