package wza.slx.com.xlxapplication.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.base.BaseActivity;
import wza.slx.com.xlxapplication.model.CommonBean;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.LoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.ModelParser;
import wza.slx.com.xlxapplication.utils.CheckUtils;

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
    private EditText et_purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initView();

    }

    private void initView() {
        et_purpose = (EditText) findViewById(R.id.et_purpose);

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
        rg_count.setOnCheckedChangeListener(this);

        rg_credit = (RadioGroup) findViewById(R.id.rg_credit);
        rg_history = (RadioGroup) findViewById(R.id.rg_history);
        rg_days = (RadioGroup) findViewById(R.id.rg_days);
        rg_count = (RadioGroup) findViewById(R.id.rg_count);

        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!credit) {
            Toast.makeText(this, getString(R.string.toast_ques_credit), Toast.LENGTH_SHORT).show();
            return;
        }
        String a_credit = "";
        switch (rg_credit.getCheckedRadioButtonId()) {
            case R.id.rb_credit_have:
                a_credit = "是";
                break;
            case R.id.rb_credit_none:
                a_credit = "否";
                break;
        }
        if (!history) {
            Toast.makeText(this, getString(R.string.toast_ques_loan_history), Toast.LENGTH_SHORT).show();
            return;
        }
        String a_histroy = "";
        switch (rg_history.getCheckedRadioButtonId()) {
            case R.id.rb_history_have:
                a_histroy = "是";
                break;
            case R.id.rb_history_none:
                a_histroy = "否";
                break;
        }

        if (!days) {
            Toast.makeText(this, getString(R.string.toast_ques_days), Toast.LENGTH_SHORT).show();
            return;
        }
        String a_days = "";
        switch (rg_days.getCheckedRadioButtonId()) {
            case R.id.rb_days_7:
                a_days = "7";
                break;
            case R.id.rb_days_14:
                a_days = "14";
                break;
            case R.id.rb_days_21:
                a_days = "21";
                break;
            case R.id.rb_days_30:
                a_days = "30";
                break;

        }
        if (!count) {
            Toast.makeText(this, getString(R.string.toast_ques_money_count), Toast.LENGTH_SHORT).show();
            return;
        }
        String a_count = "";
        switch (rg_count.getCheckedRadioButtonId()) {
            case R.id.rb_count_500:
                a_count = "500";
                break;
            case R.id.rb_count_1000:
                a_count = "1000";
                break;
            case R.id.rb_count_2000:
                a_count = "2000";
                break;
            case R.id.rb_count_3000:
                a_count = "3000";
                break;
            case R.id.rb_count_5000:
                a_count = "5000";
                break;

        }
        String purpose = et_purpose.getText().toString();
        if (TextUtils.isEmpty(purpose)) {
            Toast.makeText(this, getString(R.string.toast_ques_purpose), Toast.LENGTH_SHORT).show();
            return;
        }
        NetApi.saveQuestionnaire(this, a_credit, a_histroy, a_days, a_count, purpose,
                new LoadingCallback<CommonBean>(this, new ModelParser<CommonBean>(CommonBean.class)) {
                    @Override
                    public void onSuccess(int code, CommonBean commonBean) {
                        super.onSuccess(code, commonBean);
                        if (!CheckUtils.isNull(commonBean) && !CheckUtils.isNull(commonBean.code) &&
                                "0000".equals(commonBean.code)) {
                            Toast.makeText(QuestionActivity.this, commonBean.msg, Toast.LENGTH_SHORT).show();
                        } else if (!CheckUtils.isNull(commonBean) && !CheckUtils.isNull(commonBean.msg)) {
                            Toast.makeText(QuestionActivity.this, commonBean.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
