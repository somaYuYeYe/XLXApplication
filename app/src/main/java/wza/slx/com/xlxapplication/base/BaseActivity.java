package wza.slx.com.xlxapplication.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by homelink on 2017/3/18.
 */

public class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
