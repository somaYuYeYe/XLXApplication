package wza.slx.com.xlxapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

/**
 * Created by homelink on 2017/3/19.
 */

public class FindPwd1Activity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd1);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindPwd1Activity.this,FindPwd2Activity.class);
                startActivity(intent);
            }
        });

    }
}
