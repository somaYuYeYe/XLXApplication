package wza.slx.com.xlxapplication.net.http.callback;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import wza.slx.com.xlxapplication.R;
import wza.slx.com.xlxapplication.net.http.exception.HttpException;
import wza.slx.com.xlxapplication.net.http.exception.ServiceException;
import wza.slx.com.xlxapplication.net.http.parser.BaseParser;
import wza.slx.com.xlxapplication.utils.LogUtil;

/**
 * 无进度条的callback
 */
public abstract class NoLoadingCallback<T> extends OkCallback<T> {

    protected Context context;

    public NoLoadingCallback(Context context, BaseParser<T> mParser) {
        super(mParser);
        this.context = context;
    }


    @Override
    public void onFailure(Throwable e) {
//        super.onFailure(e);
        if (e instanceof HttpException) {
            LogUtil.d("tag", "=======http" + ((HttpException) e).getStatusCode());
            Toast.makeText(context,"网络连接错误,请稍后重试",Toast.LENGTH_SHORT).show();
        } else if (e instanceof ServiceException) {
            ServiceException se = (ServiceException) e;
            LogUtil.d("tag", "=======ServiceException:" + se.getMessage());
            Toast.makeText(context,se.getMessage(),Toast.LENGTH_SHORT).show();

        } else {
            LogUtil.d("tag", "=======e:" + e);
            e.printStackTrace();
        }
    }
}
