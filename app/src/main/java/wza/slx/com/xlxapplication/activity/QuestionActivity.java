package wza.slx.com.xlxapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;

/**
 * Created by homelink on 2017/3/19.
 */

public class QuestionActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioButton[] rbs_credit;

    private RadioGroup rg_credit;
    private RadioGroup rg_history;
    private RadioGroup rg_days;
    private RadioGroup rg_count;
    private boolean credit;
    private boolean history;
    private boolean days;
    private boolean count;

    private TextView tv_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initView();

    }

    private void initView() {
        rbs_credit = new RadioButton[13];
        rbs_credit[0] = (RadioButton) findViewById(R.id.rb_credit_have);
        rbs_credit[1] = (RadioButton) findViewById(R.id.rb_credit_none);

        rbs_credit[2] = (RadioButton) findViewById(R.id.rb_history_have);
        rbs_credit[3] = (RadioButton) findViewById(R.id.rb_history_none);

        rbs_credit[4] = (RadioButton) findViewById(R.id.rb_days_7);
        rbs_credit[5] = (RadioButton) findViewById(R.id.rb_days_14);
        rbs_credit[6] = (RadioButton) findViewById(R.id.rb_days_21);
        rbs_credit[7] = (RadioButton) findViewById(R.id.rb_days_30);

        rbs_credit[8] = (RadioButton) findViewById(R.id.rb_count_500);
        rbs_credit[9] = (RadioButton) findViewById(R.id.rb_count_1000);
        rbs_credit[10] = (RadioButton) findViewById(R.id.rb_count_2000);
        rbs_credit[11] = (RadioButton) findViewById(R.id.rb_count_3000);
        rbs_credit[12] = (RadioButton) findViewById(R.id.rb_count_5000);

        rg_credit = (RadioGroup) findViewById(R.id.rg_credit);
        rg_credit.setOnCheckedChangeListener(this);
        rg_history = (RadioGroup) findViewById(R.id.rg_history);
        rg_history.setOnCheckedChangeListener(this);
        rg_days = (RadioGroup) findViewById(R.id.rg_days);
        rg_days.setOnCheckedChangeListener(this);
        rg_count = (RadioGroup) findViewById(R.id.rg_count);
        rg_credit.setOnCheckedChangeListener(this);

        rg_credit = (RadioGroup) findViewById(R.id.rg_credit);
        rg_history = (RadioGroup) findViewById(R.id.rg_history);
        rg_days = (RadioGroup) findViewById(R.id.rg_days);
        rg_count = (RadioGroup) findViewById(R.id.rg_count);

        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(!credit){
            Toast.makeText(this, getString(R.string.toast_phone_err), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup rg, int id) {
        for (RadioButton rgs : rbs_credit) {
            if (rgs.isChecked()) {
                rgs.setBackgroundColor(getResources().getColor(R.color.gray_f6));
            } else {
                rgs.setBackgroundColor(getResources().getColor(R.color.color_white));
            }
        }
        switch (rg.getId()) {
            case R.id.rg_credit:
                credit = true;
                break;
            case R.id.rg_history:
                history = true;
                break;
            case R.id.rg_days:
                days = true;
                break;
            case R.id.rg_count:
                count = true;
                break;

        }
    }
}
