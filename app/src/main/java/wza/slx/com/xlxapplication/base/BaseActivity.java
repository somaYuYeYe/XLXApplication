package wza.slx.com.xlxapplication.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;

import wza.slx.com.xlxapplication.net.NetApi;

/**
 * Created by homelink on 2017/3/18.
 */

public class BaseActivity extends Activity {

    public static final int msg_token = 9998;
    public static final int msg_task = 101;

    private Handler mHander = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_token:
                    if (msg.obj instanceof Runnable) {
                        Runnable task = (Runnable) msg.obj;
                        NetApi.apiWrap(BaseActivity.this, task);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void postTask(Runnable task) {
        runOnUiThread(task);
    }

}
