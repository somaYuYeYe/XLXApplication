package wza.slx.com.xlxapplication.net.http.callback;

import android.app.Activity;
import android.content.Context;

import wza.slx.com.xlxapplication.net.LoadingProgressDialog;
import wza.slx.com.xlxapplication.net.http.parser.BaseParser;

public class ShowLoadingCallback<T> extends OkCallback<T> {
    protected Context context;

    public ShowLoadingCallback(Context context, BaseParser<T> mParser) {
        super(mParser);
        this.context = context;
    }


    private ShowLoadingCallback(BaseParser<T> mParser) {
        super(mParser);
    }

    @Override
    public void onSuccess(int code, T t) {
        dismissProgress();
    }

    @Override
    public void onFailure(Throwable e) {
        dismissProgress();
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress();
    }


    public boolean showProgress() {
        if (context == null) {
            return false;
        }
        if (LoadingProgressDialog.getDialog() != null
                && LoadingProgressDialog.isShowing() && context instanceof Activity)
            LoadingProgressDialog.dismiss();

        LoadingProgressDialog.show(context);
        return true;
    }

    public void dismissProgress() {
        if (context == null) {
            return;
        }
        if (LoadingProgressDialog.getDialog() != null && LoadingProgressDialog.isShowing())
            LoadingProgressDialog.dismiss();
    }
}
